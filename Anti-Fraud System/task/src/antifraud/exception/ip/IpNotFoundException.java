package antifraud.exception.ip;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Such IP-address not found in database!")
public class IpNotFoundException extends RuntimeException {
}