package zerobase.reservation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum Error {

    EMAIL_ALREADY_EXISTS("해당 이메일로 가입한 회원이 존재합니다.", HttpStatus.CONFLICT),
    PHONE_ALREADY_EXISTS("해당 휴대전화번호로 가입한 회원이 존재합니다.", HttpStatus.CONFLICT),
    NICKNAME_ALREADY_EXISTS("해당 닉네임으로 가입한 회원이 존재합니다.", HttpStatus.CONFLICT),

    EMAIL_NOT_FOUND("해당 이메일로 가입한 회원정보가 없습니다.", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND("해당 아이디의 회원정보가 없습니다.", HttpStatus.NOT_FOUND),
    STORE_NOT_FOUND("해당 아이디의 매장이 없습니다.", HttpStatus.NOT_FOUND),
    RESERVATION_NOT_FOUND("해당 아이디의 예약이 없습니다.", HttpStatus.NOT_FOUND),
    CUSTOMER_NOT_FOUND("해당 휴대전화번호로 가입한 고객이 없습니다.", HttpStatus.NOT_FOUND),
    NO_MANAGED_STORE("해당 매니저가 관리하는 매장은 없습니다.", HttpStatus.NOT_FOUND),

    INVALID_PASSWORD("비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),

    UNAUTHORIZED_MODIFICATION("해당 예약을 수정할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    UNAUTHORIZED_RESERVATION_ACCESS("해당 예약을 조회할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    UNAUTHORIZED_REVIEW_WRITE("리뷰를 작성할 권한이 없습니다.", HttpStatus.FORBIDDEN),

    PAST_DATE_NOT_ALLOWED("과거의 날짜는 예약할 수 없습니다.", HttpStatus.BAD_REQUEST),
    TODAY_DATE_NOT_ALLOWED("당일 예약은 매장 전화로 문의하시기 바랍니다.", HttpStatus.BAD_REQUEST),
    AFTER_MONTH_NOT_ALLOWED("예약은 한 달 후까지만 가능합니다.", HttpStatus.BAD_REQUEST),
    INVALID_RESERVATION_STATUS("요청한 예약상태 값이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    ARRIVAL_CHECK_BEFORE_30_MINUTES("도착 확인은 예약시간 30분 전부터 가능합니다.", HttpStatus.BAD_REQUEST),
    ARRIVAL_CHECK_TIME_EXPIRED("예약시간이 지났습니다. 카운터에 문의하시기 바랍니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

}
