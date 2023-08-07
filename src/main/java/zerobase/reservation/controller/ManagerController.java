package zerobase.reservation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import zerobase.reservation.dto.ManagerDto;
import zerobase.reservation.dto.ReservationDto;
import zerobase.reservation.dto.StoreDto;
import zerobase.reservation.entity.Manager;
import zerobase.reservation.service.ManagerService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/manager")
public class ManagerController {

    private final ManagerService managerService;

    // 매장관리자 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid ManagerDto.SignUp request) {
        return ResponseEntity.ok(managerService.signup(request));
    }

    // 매장관리자 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid ManagerDto.Login request) {
        return ResponseEntity.ok(managerService.login(request));
    }

    // 매장 등록
    @PostMapping("/store")
    public ResponseEntity<String> addStore(@AuthenticationPrincipal Manager manager,
                                           @RequestBody @Valid StoreDto.Registration request) {
        return ResponseEntity.ok(managerService.addStore(manager, request));
    }

    // 승인 대기중인 예약목록 조회
    // 파라미터 : receipt => 순서 : 예약 들어온 순서 = 접수일 순서
    // 파라미터 : visit => 순서 : 방문 예약일 순서 = 방문일 순서
    @GetMapping("/reservation/pending")
    public ResponseEntity<List<ReservationDto.Read>> getPendingReservations(@RequestParam String sort,
                                                                            @AuthenticationPrincipal Manager manager) {

        List<ReservationDto.Read> reservationList;

        switch (sort) {
            default:
            case "receipt":
                reservationList = managerService.getPendingReservationsByReceiptOrder(manager);
                break;
            case "visit":
                reservationList = managerService.getPendingReservationsByVisitOrder(manager);
                break;
        }

        return ResponseEntity.ok(reservationList);
    }

    // 입력 날짜의 전체 예약목록 조회 - 파라미터 : 검색일 / 순서 : 예약시간순
    @GetMapping("/reservation")
    public ResponseEntity<List<ReservationDto.Read>> getReservationsByDate(@AuthenticationPrincipal Manager manager,
                                                                           @RequestParam
                                                                           @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                                           LocalDate searchDate) {
        return ResponseEntity.ok(managerService.getReservationsByDate(manager, searchDate));
    }

    // 예약의 status 변경 => 승인 or 거절 or 이용완료 처리 등 가능
    @PutMapping("/reservation/{reservationId}/status")
    public ResponseEntity<String> updateReservationStatus(@PathVariable Long reservationId,
                                                          @AuthenticationPrincipal Manager manager,
                                                          @RequestParam String newStatus) {
        return ResponseEntity.ok(managerService.updateReservationStatus(reservationId, manager, newStatus));
    }

}
