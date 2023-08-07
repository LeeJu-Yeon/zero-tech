package zerobase.reservation.service;

import zerobase.reservation.dto.CustomerDto;
import zerobase.reservation.dto.ReservationDto;
import zerobase.reservation.dto.ReviewDto;
import zerobase.reservation.entity.Customer;

import java.util.List;

public interface CustomerService {

    // 매장이용자 회원가입
    String signup(CustomerDto.SignUp request);

    // 매장이용자 로그인
    String login(CustomerDto.Login request);

    // 매장 예약
    String makeReservation(Long storeId, Customer customer, ReservationDto.Make request);

    // 예약목록 조회 ( 매장이용자용 ) - 순서 : 최신순 = 최근 예약을 신청한 순서
    List<ReservationDto.Read> getReservations(Customer customer);

    // 예약 상세조회 ( 매장이용자용 )
    ReservationDto.Read getReservationDetails(Long reservationId, Customer customer);

    // 이용완료한 예약건에 대해 리뷰 작성
    String createReview(Long reservationId, Customer customer, ReviewDto.Write request);

}
