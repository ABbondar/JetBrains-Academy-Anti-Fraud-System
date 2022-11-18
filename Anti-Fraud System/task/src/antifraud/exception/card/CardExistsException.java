package antifraud.exception.card;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Such Card is already exists in database!")
public class CardExistsException extends RuntimeException {
}