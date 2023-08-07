package zerobase.reservation.service;

import zerobase.reservation.dto.ManagerDto;
import zerobase.reservation.dto.ReservationDto;
import zerobase.reservation.dto.StoreDto;
import zerobase.reservation.entity.Manager;

import java.time.LocalDate;
import java.util.List;

public interface ManagerService {

    // 매장관리자 회원가입
    String signup(ManagerDto.SignUp request);

    // 매장관리자 로그인
    String login(ManagerDto.Login request);

    // 매장 등록
    String addStore(Manager manager, StoreDto.Registration request);

    // 승인 대기중인 예약목록 조회 - 파라미터 : receipt => 순서 : 예약 들어온 순서 = 접수일 순서
    List<ReservationDto.Read> getPendingReservationsByReceiptOrder(Manager manager);

    // 승인 대기중인 예약목록 조회 - 파라미터 : visit => 순서 : 방문 예약일 순서 = 방문일 순서
    List<ReservationDto.Read> getPendingReservationsByVisitOrder(Manager manager);

    // 입력 날짜의 전체 예약목록 조회 - 파라미터 : 검색일 / 순서 : 예약시간순
    List<ReservationDto.Read> getReservationsByDate(Manager manager, LocalDate searchDate);

    // 예약의 status 변경 => 승인 or 거절 or 이용완료 처리 등 가능
    String updateReservationStatus(Long reservationId, Manager manager, String newStatus);

}
