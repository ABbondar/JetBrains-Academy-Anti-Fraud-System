package antifraud.controller;

import antifraud.dto.TransactionResultDto;
import antifraud.model.Transaction;
import antifraud.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/api/antifraud/transaction")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> verifyTransaction(@Valid @RequestBody Transaction transaction) {

        TransactionResultDto dto =
                new TransactionResultDto(
                        transactionService.processTransaction(transaction).getResult());

        return ResponseEntity.ok().body(dto);
    }
}