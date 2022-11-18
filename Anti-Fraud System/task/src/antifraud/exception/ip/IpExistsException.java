package antifraud.exception.ip;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Such IP-address is already exists in database!")
public class IpExistsException extends RuntimeException {
}