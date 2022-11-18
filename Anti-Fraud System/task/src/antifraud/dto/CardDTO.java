package antifraud.dto;

import antifraud.model.Card;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CardDTO {
    private long id;
    private String number;

    public static CardDTO mapToCardDTO(Card card) {
        return new CardDTO(
                card.getId(),
                card.getNumber());
    }
}