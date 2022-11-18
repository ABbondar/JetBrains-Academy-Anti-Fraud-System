package antifraud.exception.ip;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Such IP-address is not valid!")
public class IpNotValidException extends RuntimeException {
}