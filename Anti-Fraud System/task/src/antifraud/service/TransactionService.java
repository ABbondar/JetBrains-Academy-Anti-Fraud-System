package antifraud.service;

import antifraud.model.TransactionResult;
import antifraud.model.Transaction;

import java.util.List;

public interface TransactionService {

    TransactionResult processTransaction(Transaction transaction);

    List<Transaction> getAll();

    Transaction save(Transaction transaction);
}