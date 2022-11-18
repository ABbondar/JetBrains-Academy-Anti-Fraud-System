package antifraud.exception.role;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Such Role not found in database!")
public class RoleNotFoundException extends RuntimeException {
}