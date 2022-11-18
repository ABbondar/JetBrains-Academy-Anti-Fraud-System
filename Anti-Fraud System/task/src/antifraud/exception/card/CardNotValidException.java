package antifraud.exception.card;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Such Card is not valid!")
public class CardNotValidException extends RuntimeException {
}