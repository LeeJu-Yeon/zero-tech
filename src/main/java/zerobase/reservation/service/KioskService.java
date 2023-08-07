package zerobase.reservation.service;

import zerobase.reservation.dto.ReservationDto;
import zerobase.reservation.entity.Manager;

import java.util.List;

public interface KioskService {

    /*
    키오스크는 부팅시 매니저 아이디로 로그인 돼있고, "/kiosk/**" 경로만 접속할 수 있다고 가정.
    키오스크에서 입력받는것은 고객의 휴대전화번호 한가지.
     */

    // 휴대전화번호 입력시, 예약목록 조회
    // 해당 매장의 당일 예약 중 '숭인' or '도착' 상태인 것만 조회
    List<ReservationDto.Read> getReservationsByPhone(Manager manager, String phone);

    // 위의 예약목록 조회 결과 중 '승인' 상태인 예약건의 '도착 확인' 버튼 클릭시,
    // 해당 예약의 status 를 ARRIVED 으로 변경 ( 이미 '도착' 처리건은 확인만 가능하고 버튼은 없다 가정 )
    // 예약시간 ~ 30분 전 사이에만 가능
    String confirmArrival(Long reservationId, Manager manager);

}
