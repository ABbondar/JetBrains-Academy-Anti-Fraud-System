package antifraud.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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
    private long transactionId;

    @Min(value = 1)
    @JsonProperty("amount")
    private Long amount;

    @JsonProperty("ip")
    @NotBlank(message = "ip is required")
    private String ipAddress;

    @JsonProperty("number")
    @NotBlank(message = "number is required")
    private String cardNumber;

    @JsonProperty("region")
    @NotBlank(message = "region is required")
    @Pattern(regexp = "EAP|ECA|HIC|LAC|MENA|SA|SSA")
    private String region;

    @JsonProperty("date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;

    @JsonProperty("result")
    private String status;

    @JsonProperty("feedback")
    private String feedback = "";
}