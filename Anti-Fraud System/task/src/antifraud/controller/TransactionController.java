package antifraud.controller;

import antifraud.model.Response;
import antifraud.model.Transaction;
import antifraud.service.impl.TransactionServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/antifraud")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionServiceImpl transactionService;

    @PostMapping("/transaction")
    public ResponseEntity<?> verifyTransaction(@Valid @RequestBody Transaction transaction) {
        log.info("[POST] Request to verify transaction");

        var amount = transaction.getAmount();
        if (amount == null || amount <= 0L) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        var transactionStatus =
                transactionService.validateTransaction(amount);

        return ResponseEntity.ok(new Response(transactionStatus.name()));
    }
}