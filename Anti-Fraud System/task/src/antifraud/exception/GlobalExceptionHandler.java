package antifraud.exception;

import antifraud.dto.exception.ExceptionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidTransactionException.class)
    public ResponseEntity<ExceptionDto> handleNotAvailableSeatException(InvalidTransactionException ex) {

        log.error("Handler 'InvalidTransactionException' catch 'InvalidTransactionException'");
        ExceptionDto exception = new ExceptionDto(ex.getMessage());

        ExceptionDto e = new ExceptionDto(
                LocalDateTime.now(),
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }
}