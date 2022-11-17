package antifraud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Such Role doesn't exist!")
public class RoleNotFoundException extends RuntimeException {
}