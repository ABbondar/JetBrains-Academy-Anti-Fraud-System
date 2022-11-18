package antifraud.controller;

import antifraud.dto.CardDTO;
import antifraud.model.Card;
import antifraud.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/antifraud")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @GetMapping("/stolencard")
    public ResponseEntity<?> getAllCards() {
        log.info("[GET] Request to read all Card-numbers");

        return ResponseEntity.ok(cardService.getAll());
    }

    @PostMapping("/stolencard")
    public ResponseEntity<?> saveCard(@Validated @RequestBody Card card) {
        log.info("[POST] Request to save Card-number");

        var c = cardService.save(card);
        return new ResponseEntity<>(CardDTO.mapToCardDTO(c), HttpStatus.OK);
    }

    @DeleteMapping("/stolencard/{number}")
    public ResponseEntity<?> deleteCard(@PathVariable String number) {
        log.info("[DELETE] Request to delete Card-number");

        cardService.delete(number);
        return ResponseEntity.ok(Map.of(
                "status", "Card " + number + " successfully removed!"));
    }
}