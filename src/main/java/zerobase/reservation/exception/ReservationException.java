package zerobase.reservation.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import zerobase.reservation.enums.Error;

@Getter
public class ReservationException extends RuntimeException {

    private final Error error;
    private final String message;
    private final HttpStatus httpStatus;

    public ReservationException(Error error) {
        this.error = error;
        this.message = error.getMessage();
        this.httpStatus = error.getHttpStatus();
    }

}
