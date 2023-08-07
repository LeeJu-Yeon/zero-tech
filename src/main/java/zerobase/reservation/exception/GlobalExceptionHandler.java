package zerobase.reservation.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<String> handleMemberException(MemberException e) {
        log.error("MemberException", e);
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }

    @ExceptionHandler(ReservationException.class)
    public ResponseEntity<String> handleReservationException(ReservationException e) {
        log.error("ReservationException", e);
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }

    @ExceptionHandler(ReviewException.class)
    public ResponseEntity<String> handleReviewException(ReviewException e) {
        log.error("ReviewException", e);
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }

    @ExceptionHandler(StoreException.class)
    public ResponseEntity<String> handleStoreException(StoreException e) {
        log.error("StoreException", e);
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("Exception", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

}
