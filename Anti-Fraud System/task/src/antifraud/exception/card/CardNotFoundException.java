package antifraud.exception.card;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Such Card not found in database!")
public class CardNotFoundException extends RuntimeException {
}