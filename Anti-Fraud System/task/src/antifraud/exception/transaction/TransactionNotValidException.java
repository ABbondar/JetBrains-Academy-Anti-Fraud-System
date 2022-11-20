package antifraud.exception.transaction;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Such transaction is not valid!")
public class TransactionNotValidException extends RuntimeException{
}