package antifraud.service.impl;

import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl {

    public enum TransactionStatus {
        ALLOWED, PROHIBITED, MANUAL_PROCESSING
    }

    public TransactionStatus validateTransaction(long amount) {
        return amount <= 200L ? TransactionStatus.ALLOWED
                : amount <= 1500L ? TransactionStatus.MANUAL_PROCESSING
                : TransactionStatus.PROHIBITED;
    }
}