package antifraud.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Such User is already exists in database!")
public class UserExistsException extends RuntimeException {
}