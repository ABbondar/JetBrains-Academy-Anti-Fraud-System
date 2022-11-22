package antifraud.service;

import antifraud.model.Feedback;
import antifraud.model.TransactionResult;
import antifraud.model.Transaction;

import java.util.List;

public interface TransactionService {

    List<Transaction> getAll();

    List<Transaction> getAll(String cardNumber);

    TransactionResult processTransaction(Transaction transaction);

    Transaction processTransactionWithFeedback(Feedback request);

    Transaction save(Transaction transaction);
}