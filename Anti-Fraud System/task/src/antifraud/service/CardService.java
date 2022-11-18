package antifraud.service;

import antifraud.dto.CardDTO;
import antifraud.model.Card;

import java.util.List;

public interface CardService {

    List<CardDTO> getAll();

    Card save(Card card);

    void delete(String number);
}