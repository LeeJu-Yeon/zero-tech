package zerobase.reservation.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import zerobase.reservation.dto.ManagerDto;
import zerobase.reservation.dto.ReservationDto;
import zerobase.reservation.dto.StoreDto;
import zerobase.reservation.entity.Manager;
import zerobase.reservation.entity.Reservation;
import zerobase.reservation.entity.Store;
import zerobase.reservation.enums.Error;
import zerobase.reservation.enums.ReservationStatus;
import zerobase.reservation.exception.MemberException;
import zerobase.reservation.exception.ReservationException;
import zerobase.reservation.exception.StoreException;
import zerobase.reservation.repository.ManagerRepository;
import zerobase.reservation.repository.ReservationRepository;
import zerobase.reservation.repository.StoreRepository;
import zerobase.reservation.security.TokenProvider;
import zerobase.reservation.service.ManagerService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
    private final StoreRepository storeRepository;
    private final ReservationRepository reservationRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    // 매장관리자 회원가입
    @Override
    public String signup(ManagerDto.SignUp request) {

        // 이메일 중복 체크
        if (managerRepository.existsByEmail(request.getEmail())) {
            throw new MemberException(Error.EMAIL_ALREADY_EXISTS);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 휴대전화번호 중복 체크
        if (managerRepository.existsByPhone(request.getPhone())) {
            throw new MemberException(Error.PHONE_ALREADY_EXISTS);
        }

        managerRepository.save(Manager.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .name(request.getName())
                .phone(request.getPhone())
                .build());

        return "매장관리자 회원가입 성공";
    }

    // 매장관리자 로그인
    @Override
    public String login(ManagerDto.Login request) {

        // 이메일 존재하나 확인
        Manager manager = managerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new MemberException(Error.EMAIL_NOT_FOUND));

        // 비밀번호 일치하나 확인
        if (!passwordEncoder.matches(request.getPassword(), manager.getPassword())) {
            throw new MemberException(Error.INVALID_PASSWORD);
        }

        // 모두 통과시 토큰 발행
        return tokenProvider.generateToken(manager.getId(), manager.getEmail(), manager.getMemberType());
    }

    // 매장 등록
    @Override
    public String addStore(Manager manager, StoreDto.Registration request) {

        storeRepository.save(Store.builder()
                .manager(manager)
                .name(request.getName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .description(request.getDescription())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build());

        return "매장 등록 성공";
    }

    // 승인 대기중인 예약목록 조회 - 파라미터 : receipt => 순서 : 예약 들어온 순서 = 접수일 순서
    @Override
    public List<ReservationDto.Read> getPendingReservationsByReceiptOrder(Manager manager) {

        // 매니저가 관리하는 매장 조회
        Store store = storeRepository.findByManager(manager)
                .orElseThrow(() -> new StoreException(Error.NO_MANAGED_STORE));

        // 매장, 예약상태 = PENDING, 생성일 오름차순 조건으로 예약목록 조회
        List<Reservation> reservationList = reservationRepository
                .findAllByStoreAndStatusOrderByCreatedAtAsc(store, ReservationStatus.PENDING);

        // DTO 리스트로 변환해서 반환
        return ReservationDto.Read.getDtoListFromReservationList(reservationList);
    }

    // 승인 대기중인 예약목록 조회 - 파라미터 : visit => 순서 : 방문 예약일 순서 = 방문일 순서
    @Override
    public List<ReservationDto.Read> getPendingReservationsByVisitOrder(Manager manager) {

        // 매니저가 관리하는 매장 조회
        Store store = storeRepository.findByManager(manager)
                .orElseThrow(() -> new StoreException(Error.NO_MANAGED_STORE));

        // 매장, 예약상태 = PENDING, 예약일 오름차순 조건으로 예약목록 조회
        List<Reservation> reservationList = reservationRepository
                .findAllByStoreAndStatusOrderByReservationDateAsc(store, ReservationStatus.PENDING);

        // DTO 리스트로 변환해서 반환
        return ReservationDto.Read.getDtoListFromReservationList(reservationList);
    }

    // 입력 날짜의 전체 예약목록 조회 - 파라미터 : 검색일 / 순서 : 예약시간순
    @Override
    public List<ReservationDto.Read> getReservationsByDate(Manager manager, LocalDate searchDate) {

        // 매니저가 관리하는 매장 조회
        Store store = storeRepository.findByManager(manager)
                .orElseThrow(() -> new StoreException(Error.NO_MANAGED_STORE));

        // 매장, 예약일 = searchDate, 예약시간 오름차순 조건으로 예약목록 조회
        List<Reservation> reservationList = reservationRepository
                .findAllByStoreAndReservationDateOrderByReservationTime(store, searchDate);

        // DTO 리스트로 변환해서 반환
        return ReservationDto.Read.getDtoListFromReservationList(reservationList);
    }

    // 예약의 status 변경 => 승인 or 거절 or 이용완료 처리 등 가능
    @Override
    public String updateReservationStatus(Long reservationId, Manager manager, String newStatus) {

        // 해당 아이디의 예약이 존재하는지 확인
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException(Error.RESERVATION_NOT_FOUND));

        // 매니저가 관리하는 매장 조회
        Store store = storeRepository.findByManager(manager)
                .orElseThrow(() -> new StoreException(Error.NO_MANAGED_STORE));

        // 해당 아이디의 예약이, 매니저의 매장것인지 확인
        if (reservation.getStore().getId() != store.getId()) {
            throw new ReservationException(Error.UNAUTHORIZED_MODIFICATION);
        }

        // 이전 상태 기록
        String beforeStatus = reservation.getStatus().getStatusString();

        // 요청한 상태가 올바른 값이면 셋팅
        try {
            reservation.setStatus(ReservationStatus.valueOf(newStatus));
        } catch (IllegalArgumentException e) {
            throw new ReservationException(Error.INVALID_RESERVATION_STATUS);
        }

        // 수정후 상태 기록
        String afterStatus = reservation.getStatus().getStatusString();

        // 수정후 예약 저장
        reservationRepository.save(reservation);

        return "해당 예약의 상태를 <" + beforeStatus + ">에서 <" + afterStatus + ">(으)로 변경하였습니다.";
    }

}
