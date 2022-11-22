package antifraud.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "limits")
public class Limit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    @JsonProperty("number")
    private String cardNumber;

    @JsonProperty("maxAllowed")
    private long maxAllowed = 200;

    @JsonProperty("maxManual")
    private long maxManual = 1500;
}