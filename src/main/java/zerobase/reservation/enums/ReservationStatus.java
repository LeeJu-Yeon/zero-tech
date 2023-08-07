package zerobase.reservation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatus {

    PENDING("대기"),     // 승인 대기중
    APPROVED("승인"),    // 승인
    REJECTED("거절"),    // 거절
    ARRIVED("도착"),     // 손님 도착
    NO_SHOW("노쇼"),     // 손님 안옴
    COMPLETED("완료");   // 이용 완료

    private final String statusString;

}
