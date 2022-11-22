package antifraud.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Feedback {
    private long transactionId;

    @Pattern(regexp = "ALLOWED|MANUAL_PROCESSING|PROHIBITED")
    private String feedback;
}