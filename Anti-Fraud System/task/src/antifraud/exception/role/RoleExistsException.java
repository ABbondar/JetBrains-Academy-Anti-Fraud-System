package antifraud.exception.role;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Such Role is already exists in database!")
public class RoleExistsException extends RuntimeException {
}