package zerobase.reservation.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import zerobase.reservation.enums.Error;

@Getter
public class StoreException extends RuntimeException {

    private final Error error;
    private final String message;
    private final HttpStatus httpStatus;

    public StoreException(Error error) {
        this.error = error;
        this.message = error.getMessage();
        this.httpStatus = error.getHttpStatus();
    }

}
