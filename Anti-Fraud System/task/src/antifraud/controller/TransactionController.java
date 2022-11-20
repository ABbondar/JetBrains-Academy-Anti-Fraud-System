package antifraud.controller;

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
    public ResponseEntity<?> processTransaction(@Valid @RequestBody Transaction transaction) {
        log.info("[POST] Request to process transaction");

        var amount = transaction.getAmount();
        if (amount == null || amount <= 0L) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        var response = transactionService.processTransaction(transaction);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transaction")
    public ResponseEntity<?> getAllTransactions() {
        log.info("[POST] Request to read all transactions");

        return ResponseEntity.ok(transactionService.getAll());
    }
}