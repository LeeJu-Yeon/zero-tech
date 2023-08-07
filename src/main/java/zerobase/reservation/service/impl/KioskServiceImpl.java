package zerobase.reservation.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zerobase.reservation.dto.ReservationDto;
import zerobase.reservation.entity.Customer;
import zerobase.reservation.entity.Manager;
import zerobase.reservation.entity.Reservation;
import zerobase.reservation.entity.Store;
import zerobase.reservation.enums.Error;
import zerobase.reservation.enums.ReservationStatus;
import zerobase.reservation.exception.MemberException;
import zerobase.reservation.exception.ReservationException;
import zerobase.reservation.exception.StoreException;
import zerobase.reservation.repository.CustomerRepository;
import zerobase.reservation.repository.ReservationRepository;
import zerobase.reservation.repository.StoreRepository;
import zerobase.reservation.service.KioskService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KioskServiceImpl implements KioskService {

    private final StoreRepository storeRepository;
    private final CustomerRepository customerRepository;
    private final ReservationRepository reservationRepository;

    // 쿼리 검색용
    private final List<ReservationStatus> status = List.of(ReservationStatus.APPROVED, ReservationStatus.ARRIVED);

    /*
    키오스크는 부팅시 매니저 아이디로 로그인 돼있고, "/kiosk/**" 경로만 접속할 수 있다고 가정.
    키오스크에서 입력받는것은 고객의 휴대전화번호 한가지.
     */

    // 휴대전화번호 입력시, 예약목록 조회
    // 해당 매장의 당일 예약 중 '숭인' or '도착' 상태인 것만 조회
    @Override
    public List<ReservationDto.Read> getReservationsByPhone(Manager manager, String phone) {

        // 매니저의 매장 조회
        Store store = storeRepository.findByManager(manager)
                .orElseThrow(() -> new StoreException(Error.NO_MANAGED_STORE));

        // 휴대전화번호로 고객 조회
        Customer customer = customerRepository.findByPhone(phone)
                .orElseThrow(() -> new MemberException(Error.CUSTOMER_NOT_FOUND));

        // 예약목록 조회 - 조건 : 매장, 고객, 당일, '숭인' or '도착' 상태
        List<Reservation> reservationList = reservationRepository
                .getReservationsByPhone(store, customer, LocalDate.now(), status);

        List<ReservationDto.Read> result = new ArrayList<>();

        for (Reservation reservation : reservationList) {

            // 본인확인만 가능하게, 이름은 앞 두글자만, 휴대전화번호는 뒤 네자리만 가져오게 하였습니다
            String customerName = reservation.getCustomer().getName().substring(0, 2);
            String customerPhone = reservation.getCustomer().getPhone().substring(9, 13);

            result.add(ReservationDto.Read.builder()
                    .id(reservation.getId())
                    .customerName(customerName)
                    .customerPhone(customerPhone)
                    .reservationDate(reservation.getReservationDate())
                    .reservationTime(reservation.getReservationTime())
                    .guestCount(reservation.getGuestCount())
                    .status(reservation.getStatus())
                    .build());
        }

        return result;
    }

    // 위의 예약목록 조회 결과 중 '승인' 상태인 예약건의 '도착 확인' 버튼 클릭시,
    // 해당 예약의 status 를 ARRIVED 으로 변경 ( 이미 '도착' 처리건은 확인만 가능하고 버튼은 없다 가정 )
    // 예약시간 ~ 30분 전 사이에만 가능
    @Override
    public String confirmArrival(Long reservationId, Manager manager) {

        // 해당 아이디의 예약이 존재하는지 확인
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException(Error.RESERVATION_NOT_FOUND));

        // 매니저가 관리하는 매장 조회
        Store store = storeRepository.findByManager(manager)
                .orElseThrow(() -> new StoreException(Error.NO_MANAGED_STORE));

        // 해당 아이디의 예약이, 매니저의 매장것인지 확인
        if (!reservation.getStore().getId().equals(store.getId())) {
            throw new ReservationException(Error.UNAUTHORIZED_MODIFICATION);
        }

        // 예약시간 ~ 30분 전 사이인지 확인
        LocalTime now = LocalTime.now();
        LocalTime start = reservation.getReservationTime().minusMinutes(30);
        LocalTime end = reservation.getReservationTime();

        if (now.isBefore(start)) {
            throw new ReservationException(Error.ARRIVAL_CHECK_BEFORE_30_MINUTES);
        } else if (now.isAfter(end)) {
            throw new ReservationException(Error.ARRIVAL_CHECK_TIME_EXPIRED);
        }

        // ARRIVED 로 변경 & 저장
        reservation.setStatus(ReservationStatus.ARRIVED);
        reservationRepository.save(reservation);

        return "도착 확인 완료";
    }

}
