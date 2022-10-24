package antifraud.model;

import lombok.*;

import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Transaction {

    @Min(value = 1)
    private Long amount;
}