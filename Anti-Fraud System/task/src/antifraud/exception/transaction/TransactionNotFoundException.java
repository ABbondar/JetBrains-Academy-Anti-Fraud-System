package antifraud.exception.transaction;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Such Transaction not found in database!")
public class TransactionNotFoundException extends RuntimeException {
}