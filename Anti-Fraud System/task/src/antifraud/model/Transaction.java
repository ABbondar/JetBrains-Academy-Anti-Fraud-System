package antifraud.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    @Min(value = 1)
    private Long amount;

    @JsonProperty("ip")
    @NotBlank(message = "ip is required")
    private String ipAddress;

    @JsonProperty("number")
    @NotBlank(message = "number is required")
    private String cardNumber;

    @NotBlank(message = "region is required")
    private String region;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;
}