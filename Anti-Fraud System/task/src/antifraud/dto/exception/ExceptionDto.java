package antifraud.dto.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExceptionDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonIgnore
    private LocalDateTime timestamp;

    @JsonIgnore
    private int status;
    private String error;

    public ExceptionDto(String error) {
        this.error = error;
    }

    public ExceptionDto(int status, String error) {
        this.status = status;
        this.error = error;
    }

    public ExceptionDto(LocalDateTime timestamp, String error, int status) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
    }

    @JsonIgnore
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @JsonIgnore
    public int getStatus() {
        return status;
    }
}