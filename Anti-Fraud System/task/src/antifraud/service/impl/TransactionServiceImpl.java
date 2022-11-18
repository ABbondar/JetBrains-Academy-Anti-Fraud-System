package antifraud.service.impl;

import antifraud.model.Response;
import antifraud.model.Transaction;
import antifraud.model.TransactionStatus;
import antifraud.repository.CardRepository;
import antifraud.repository.IpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl {

    private final IpRepository ipRepository;
    private final CardRepository cardRepository;

    public Response validateTransaction(Transaction transaction) {

        var status = getTransactionStatus(transaction);
        var ip = isIpInDatabase(transaction);
        var number = isNumberInDatabase(transaction);

        List<String> violations = new ArrayList<>();

        if (status == TransactionStatus.PROHIBITED) {
            violations.add("amount");
        }
        if (number) {
            violations.add("card-number");
            status = TransactionStatus.PROHIBITED;
        }
        if (ip) {
            violations.add("ip");
            status = TransactionStatus.PROHIBITED;
        }

        String info = violations.isEmpty()
                ? status == TransactionStatus.MANUAL_PROCESSING ? "amount" : "none"
                : violations.stream().sorted().collect(Collectors.joining(", "));

        return new Response(status, info);
    }

    private TransactionStatus getTransactionStatus(Transaction transaction) {
        return transaction.getAmount() <= 200L
                ? TransactionStatus.ALLOWED
                : transaction.getAmount() <= 1500L
                ? TransactionStatus.MANUAL_PROCESSING
                : TransactionStatus.PROHIBITED;
    }

    private boolean isIpInDatabase(Transaction transaction) {
        return ipRepository.findAll()
                .stream()
                .anyMatch(i -> transaction.getIpAddress().equals(i.getIpAddress()));
    }

    private boolean isNumberInDatabase(Transaction transaction) {
        return cardRepository.findAll()
                .stream()
                .anyMatch(card -> transaction.getCardNumber().equals(card.getNumber()));
    }
}