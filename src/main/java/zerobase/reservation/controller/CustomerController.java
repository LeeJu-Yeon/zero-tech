package zerobase.reservation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import zerobase.reservation.dto.CustomerDto;
import zerobase.reservation.dto.ReservationDto;
import zerobase.reservation.dto.ReviewDto;
import zerobase.reservation.entity.Customer;
import zerobase.reservation.service.CustomerService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    // 매장이용자 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid CustomerDto.SignUp request) {
        return ResponseEntity.ok(customerService.signup(request));
    }

    // 매장이용자 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid CustomerDto.Login request) {
        return ResponseEntity.ok(customerService.login(request));
    }

    // 매장 예약
    @PostMapping("/reservation/{storeId}")
    public ResponseEntity<String> makeReservation(@PathVariable Long storeId,
                                                  @AuthenticationPrincipal Customer customer,
                                                  @RequestBody @Valid ReservationDto.Make request) {
        return ResponseEntity.ok(customerService.makeReservation(storeId, customer, request));
    }

    // 예약목록 조회 ( 매장이용자용 ) - 순서 : 최신순 = 최근 예약을 신청한 순서
    @GetMapping("/reservation")
    public ResponseEntity<List<ReservationDto.Read>> getReservations(@AuthenticationPrincipal Customer customer) {
        return ResponseEntity.ok(customerService.getReservations(customer));
    }

    // 예약 상세조회 ( 매장이용자용 )
    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<ReservationDto.Read> getReservationDetails(@PathVariable Long reservationId,
                                                                     @AuthenticationPrincipal Customer customer) {
        return ResponseEntity.ok(customerService.getReservationDetails(reservationId, customer));
    }

    // 이용완료한 예약건에 대해 리뷰 작성
    @PostMapping("/reservation/{reservationId}/review")
    public ResponseEntity<String> createReview(@PathVariable Long reservationId,
                                               @AuthenticationPrincipal Customer customer,
                                               @RequestBody @Valid ReviewDto.Write request) {
        return ResponseEntity.ok(customerService.createReview(reservationId, customer, request));
    }

}
