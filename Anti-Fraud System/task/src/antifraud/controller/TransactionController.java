package antifraud.controller;

import antifraud.model.Feedback;
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

    @GetMapping("/history")
    public ResponseEntity<?> getAllTransactions() {
        log.info("[POST] Request to read all transactions");

        var response = transactionService.getAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history/{number}")
    public ResponseEntity<?> getTransactionsHistoryByCardNumber(@PathVariable String number) {
        log.info("[POST] Request to get transactions history by Card Number");

        var response = transactionService.getAll(number);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transaction")
    public ResponseEntity<?> processTransaction(@Valid @RequestBody Transaction transaction) {
        log.info("[POST] Request to process transaction");

        var amount = transaction.getAmount();
        if (amount == null || amount <= 0L) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        var response = transactionService.processTransaction(transaction);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/transaction")
    public ResponseEntity<?> processTransactionWithFeedback(@Valid @RequestBody Feedback request) {
        log.info("[PUT] Request to process transaction with feedback");

        var response = transactionService.processTransactionWithFeedback(request);
        return ResponseEntity.ok(response);
    }
}