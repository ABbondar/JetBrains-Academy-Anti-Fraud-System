package antifraud.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Min;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Transaction {

    @Min(value = 1)
    private Long amount;

    @JsonProperty("ip")
    private String ipAddress;

    @JsonProperty("number")
    private String cardNumber;
}