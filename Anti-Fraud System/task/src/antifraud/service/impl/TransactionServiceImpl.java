package antifraud.service.impl;

import antifraud.exception.transaction.TransactionNotValidException;
import antifraud.model.Region;
import antifraud.model.TransactionResult;
import antifraud.model.Transaction;
import antifraud.model.TransactionStatus;
import antifraud.repository.CardRepository;
import antifraud.repository.IpRepository;
import antifraud.repository.TransactionRepository;
import antifraud.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final IpRepository ipRepository;
    private final CardRepository cardRepository;

    @Override
    public TransactionResult processTransaction(Transaction transaction) {
        save(transaction);
        int uniqueRegions = uniqueRegionsDuringOneHour(transaction);
        int uniqueIpAddresses = uniqueIpDuringOneHour(transaction);

        var status = getTransactionStatus(transaction);
        var ipAddress = isIpInDatabase(transaction);
        var cardNumber = isNumberInDatabase(transaction);

        List<String> violations = new ArrayList<>();

        if (status == TransactionStatus.PROHIBITED) {
            violations.add("amount");
        }

        if (cardNumber) {
            violations.add("card-number");
            status = TransactionStatus.PROHIBITED;
        }

        if (ipAddress) {
            violations.add("ip");
            status = TransactionStatus.PROHIBITED;
        }

        if (uniqueRegions > 2) {
            violations.add("region-correlation");
            status = TransactionStatus.PROHIBITED;
        }

        if (uniqueRegions == 2) {
            violations.add("region-correlation");
            status = TransactionStatus.MANUAL_PROCESSING;
        }

        if (uniqueIpAddresses > 2) {
            violations.add("ip-correlation");
            status = TransactionStatus.PROHIBITED;
        }

        if (uniqueIpAddresses == 2) {
            violations.add("ip-correlation");
            status = TransactionStatus.MANUAL_PROCESSING;
        }

        String info = violations.isEmpty()
                ? status == TransactionStatus.MANUAL_PROCESSING ? "amount" : "none"
                : violations.stream().sorted().collect(Collectors.joining(", "));

        return new TransactionResult(status, info);
    }

    @Override
    public List<Transaction> getAll() {
        return new ArrayList<>(transactionRepository.findAll());
    }

    @Override
    public Transaction save(Transaction transaction) {
        if (!isTransactionValid(transaction)) {
            throw new TransactionNotValidException();
        }
        return transactionRepository.save(transaction);
    }

    private TransactionStatus getTransactionStatus(Transaction transaction) {
        return transaction.getAmount() <= 200L
                ? TransactionStatus.ALLOWED
                : transaction.getAmount() <= 1500L
                ? TransactionStatus.MANUAL_PROCESSING
                : TransactionStatus.PROHIBITED;
    }

    private int uniqueIpDuringOneHour(Transaction transaction) {
        List<Transaction> transactions = transactionRepository
                .findByCardNumberAndDateBetween(
                        transaction.getCardNumber(),
                        transaction.getDate().minusHours(1),
                        transaction.getDate());

        Set<String> uniqueIpAddresses = new HashSet<>();

        for (Transaction t : transactions) {
            if (!transaction.getIpAddress().equals(t.getIpAddress())) {
                uniqueIpAddresses.add(t.getIpAddress());
            }
        }
        return uniqueIpAddresses.size();
    }

    private int uniqueRegionsDuringOneHour(Transaction transaction) {
        List<Transaction> transactions = transactionRepository
                .findByCardNumberAndDateBetween(
                        transaction.getCardNumber(),
                        transaction.getDate().minusHours(1),
                        transaction.getDate());

        Set<String> uniqueRegions = new HashSet<>();

        for (Transaction t : transactions) {
            if (!transaction.getRegion().equals(t.getRegion())) {
                uniqueRegions.add(t.getRegion());
            }
        }
        return uniqueRegions.size();
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

    private static boolean isTransactionValid(Transaction transaction) {
        return isIpAddressValid(transaction.getIpAddress())
                && isCardNumberValid(transaction.getCardNumber())
                && isRegionValid(transaction.getRegion());
    }

    private static boolean isIpAddressValid(String ipAddress) {
        InetAddressValidator validator = InetAddressValidator.getInstance();
        return validator.isValidInet4Address(ipAddress);
    }

    private static boolean isCardNumberValid(String number) {
        return LuhnCheckDigit.LUHN_CHECK_DIGIT.isValid(number);
    }

    private static boolean isRegionValid(String region) {
        return Region.getRegions().contains(region);
    }
}