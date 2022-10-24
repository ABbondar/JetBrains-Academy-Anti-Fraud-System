package antifraud.service;

import antifraud.exception.InvalidTransactionException;
import antifraud.model.Transaction;
import antifraud.model.TransactionResult;

import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    public TransactionResult processTransaction(Transaction transaction) {
        TransactionResult tr = new TransactionResult();

        if ((transaction.getAmount() == null) || (transaction.getAmount() <= 0)) {
            throw new InvalidTransactionException("Invalid transaction amount");
        }

        if (transaction.getAmount() <= 200) {
            tr.setResult("ALLOWED");

        } else if (transaction.getAmount() <= 1500) {
            tr.setResult("MANUAL_PROCESSING");

        } else {
            tr.setResult("PROHIBITED");
        }
        return tr;
    }
}