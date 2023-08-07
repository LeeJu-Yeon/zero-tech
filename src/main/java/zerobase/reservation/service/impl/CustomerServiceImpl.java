package zerobase.reservation.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import zerobase.reservation.dto.CustomerDto;
import zerobase.reservation.dto.ReservationDto;
import zerobase.reservation.dto.ReviewDto;
import zerobase.reservation.entity.Customer;
import zerobase.reservation.entity.Reservation;
import zerobase.reservation.entity.Review;
import zerobase.reservation.entity.Store;
import zerobase.reservation.enums.Error;
import zerobase.reservation.enums.ReservationStatus;
import zerobase.reservation.exception.MemberException;
import zerobase.reservation.exception.ReservationException;
import zerobase.reservation.exception.ReviewException;
import zerobase.reservation.exception.StoreException;
import zerobase.reservation.repository.CustomerRepository;
import zerobase.reservation.repository.ReservationRepository;
import zerobase.reservation.repository.ReviewRepository;
import zerobase.reservation.repository.StoreRepository;
import zerobase.reservation.security.TokenProvider;
import zerobase.reservation.service.CustomerService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    // 매장이용자 회원가입
    @Override
    public String signup(CustomerDto.SignUp request) {

        // 이메일 중복 체크
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new MemberException(Error.EMAIL_ALREADY_EXISTS);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 휴대전화번호 중복 체크
        if (customerRepository.existsByPhone(request.getPhone())) {
            throw new MemberException(Error.PHONE_ALREADY_EXISTS);
        }

        // 닉네임 중복 체크
        if (customerRepository.existsByNickname(request.getNickname())) {
            throw new MemberException(Error.NICKNAME_ALREADY_EXISTS);
        }

        customerRepository.save(Customer.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .name(request.getName())
                .phone(request.getPhone())
                .nickname(request.getNickname())
                .build());

        return "매장이용자 회원가입 성공";
    }

    // 매장이용자 로그인
    @Override
    public String login(CustomerDto.Login request) {

        // 이메일 존재하나 확인
        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new MemberException(Error.EMAIL_NOT_FOUND));

        // 비밀번호 일치하나 확인
        if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
            throw new MemberException(Error.INVALID_PASSWORD);
        }

        // 모두 통과시 토큰 발행
        return tokenProvider.generateToken(customer.getId(), customer.getEmail(), customer.getMemberType());
    }

    // 매장 예약
    @Override
    public String makeReservation(Long storeId, Customer customer, ReservationDto.Make request) {

        // 매장 존재하나 확인
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(Error.STORE_NOT_FOUND));

        // 예약은 다음날 ~ 한달후 까지만 가능하다고 가정
        LocalDate want = request.getReservationDate();
        LocalDate today = LocalDate.now();

        if (want.isBefore(today)) {
            throw new ReservationException(Error.PAST_DATE_NOT_ALLOWED);
        } else if (want.isEqual(today)) {
            throw new ReservationException(Error.TODAY_DATE_NOT_ALLOWED);
        } else if (want.isAfter(today.plusMonths(1))) {
            throw new ReservationException(Error.AFTER_MONTH_NOT_ALLOWED);
        }

        reservationRepository.save(Reservation.builder()
                .store(store)
                .customer(customer)
                .reservationDate(request.getReservationDate())
                .reservationTime(request.getReservationTime())
                .guestCount(request.getGuestCount())
                .status(ReservationStatus.PENDING)
                .build());

        return "예약 성공";
    }

    // 예약목록 조회 ( 매장이용자용 ) - 순서 : 최신순 = 최근 예약을 신청한 순서
    @Override
    public List<ReservationDto.Read> getReservations(Customer customer) {

        // 해당 고객의 예약 조회
        List<Reservation> reservationList = reservationRepository.findAllByCustomerOrderByCreatedAtDesc(customer);

        // DTO 리스트로 변환해서 반환
        return ReservationDto.Read.getDtoListFromReservationList(reservationList);
    }

    // 예약 상세조회 ( 매장이용자용 )
    @Override
    public ReservationDto.Read getReservationDetails(Long reservationId, Customer customer) {

        // 해당 아이디의 예약이 존재하는지 확인
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException(Error.RESERVATION_NOT_FOUND));

        // 해당 예약이 고객의 것인지 확인
        if (!reservation.getCustomer().getId().equals(customer.getId())) {
            throw new ReservationException(Error.UNAUTHORIZED_RESERVATION_ACCESS);
        }

        // DTO 로 변환해서 반환
        return ReservationDto.Read.getDtoFromReservation(reservation);
    }

    // 이용완료한 예약건에 대해 리뷰 작성
    @Override
    public String createReview(Long reservationId, Customer customer, ReviewDto.Write request) {

        // 해당 아이디의 예약이 존재하는지 확인
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException(Error.RESERVATION_NOT_FOUND));

        // 해당 예약이 고객의 것인지 확인 && 이용완료한 예약이 맞는지 확인
        if (!reservation.getCustomer().getId().equals(customer.getId()) ||
                (reservation.getStatus() != ReservationStatus.COMPLETED)) {
            throw new ReviewException(Error.UNAUTHORIZED_REVIEW_WRITE);
        }

        reviewRepository.save(Review.builder()
                .reservation(reservation)
                .store(reservation.getStore())
                .customer(customer)
                .rating(request.getRating())
                .content(request.getContent())
                .build());

        return "리뷰 작성 성공";
    }

}
