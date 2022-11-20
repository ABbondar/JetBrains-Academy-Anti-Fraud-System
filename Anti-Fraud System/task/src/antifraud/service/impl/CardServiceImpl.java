package antifraud.service.impl;

import antifraud.dto.CardDTO;
import antifraud.exception.card.CardNotFoundException;
import antifraud.exception.card.CardNotValidException;
import antifraud.model.Card;
import antifraud.repository.CardRepository;
import antifraud.service.CardService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    @Override
    public List<CardDTO> getAll() {
        return cardRepository.findAll()
                .stream()
                .map(CardDTO::mapToCardDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Card save(Card card) {
        if (!LuhnCheckDigit.LUHN_CHECK_DIGIT.isValid(card.getNumber())) {
            throw new CardNotValidException();
        }

        if (cardRepository.findAll()
                .stream()
                .anyMatch(c -> card.getNumber().equals(c.getNumber()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        return cardRepository.save(card);
    }

    @Override
    @Transactional
    public void delete(String number) {
        if (!LuhnCheckDigit.LUHN_CHECK_DIGIT.isValid(number)) {
            throw new CardNotValidException();
        }
        var card = cardRepository.findByNumber(number)
                .orElseThrow(CardNotFoundException::new);

        cardRepository.deleteByNumber(card.getNumber());
    }
}