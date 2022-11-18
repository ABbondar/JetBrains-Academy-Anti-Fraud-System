package antifraud.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Such User not found in database!")
public class UserNotFoundException extends RuntimeException {
}