package zerobase.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import zerobase.reservation.dto.ReservationDto;
import zerobase.reservation.entity.Manager;
import zerobase.reservation.service.KioskService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kiosk")
public class KioskController {

    private final KioskService kioskService;

    /*
    키오스크는 부팅시 매니저 아이디로 로그인 돼있고, "/kiosk/**" 경로만 접속할 수 있다고 가정.
    키오스크에서 입력받는것은 고객의 휴대전화번호 한가지.
     */

    // 휴대전화번호 입력시, 예약목록 조회
    // 해당 매장의 당일 예약 중 '숭인' or '도착' 상태인 것만 조회
    @GetMapping("/reservation/{phone}")
    public ResponseEntity<List<ReservationDto.Read>> getReservationsByPhone(@AuthenticationPrincipal Manager manager,
                                                                            @PathVariable String phone) {
        return ResponseEntity.ok(kioskService.getReservationsByPhone(manager, phone));
    }

    // 위의 예약목록 조회 결과 중 '승인' 상태인 예약건의 '도착 확인' 버튼 클릭시,
    // 해당 예약의 status 를 ARRIVED 으로 변경 ( 이미 '도착' 처리건은 확인만 가능하고 버튼은 없다 가정 )
    // 예약시간 ~ 30분 전 사이에만 가능
    @PutMapping("/reservation/{reservationId}/arrived")
    public ResponseEntity<String> confirmArrival(@PathVariable Long reservationId,
                                                 @AuthenticationPrincipal Manager manager) {
        return ResponseEntity.ok(kioskService.confirmArrival(reservationId, manager));
    }

}
