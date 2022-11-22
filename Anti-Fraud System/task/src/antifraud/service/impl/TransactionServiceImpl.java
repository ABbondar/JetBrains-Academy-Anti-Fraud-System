package antifraud.service.impl;

import antifraud.exception.transaction.TransactionNotFoundException;
import antifraud.exception.transaction.TransactionNotValidException;
import antifraud.model.*;
import antifraud.repository.LimitRepository;
import antifraud.repository.CardRepository;
import antifraud.repository.IpRepository;
import antifraud.repository.TransactionRepository;
import antifraud.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final IpRepository ipRepository;
    private final CardRepository cardRepository;
    private final LimitRepository limitRepository;

    private final LimitServiceImpl limitService;

    @Override
    public List<Transaction> getAll() {
        return new ArrayList<>(transactionRepository.findAll());
    }

    @Override
    public List<Transaction> getAll(String cardNumber) {
        if (!isCardNumberValid(cardNumber)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return transactionRepository.findAllByCardNumber(cardNumber)
                .orElseThrow(TransactionNotFoundException::new);
    }

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

        Transaction updatedWithStatus = transactionRepository.findById(transaction.getTransactionId())
                .orElseThrow(TransactionNotFoundException::new);

        updatedWithStatus.setStatus(status.name());
        save(updatedWithStatus);

        return new TransactionResult(status, info);
    }

    @Override
    public Transaction processTransactionWithFeedback(Feedback request) {
        limitService.updateLimit(request);

        var t = transactionRepository.findById(request.getTransactionId())
                .orElseThrow(TransactionNotFoundException::new);

        t.setFeedback(request.getFeedback());
        return save(t);
    }

    @Override
    public Transaction save(Transaction transaction) {
        if (!isTransactionValid(transaction)) {
            throw new TransactionNotValidException();
        }
        return transactionRepository.save(transaction);
    }

    private TransactionStatus getTransactionStatus(Transaction transaction) {
        if (limitRepository.findAll().isEmpty()) {
            return transaction.getAmount()
                    <= 200
                    ? TransactionStatus.ALLOWED : transaction.getAmount()

                    <= 1500
                    ? TransactionStatus.MANUAL_PROCESSING : TransactionStatus.PROHIBITED;
        } else {
            return transaction.getAmount()
                    <= limitService.getAllowedLimit(transaction.getCardNumber())
                    ? TransactionStatus.ALLOWED : transaction.getAmount()

                    <= limitService.getManualLimit(transaction.getCardNumber())
                    ? TransactionStatus.MANUAL_PROCESSING : TransactionStatus.PROHIBITED;
        }
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

    private boolean isTransactionValid(Transaction transaction) {
        return isIpAddressValid(transaction.getIpAddress())
                && isCardNumberValid(transaction.getCardNumber());
    }

    private boolean isIpAddressValid(String ipAddress) {
        InetAddressValidator validator = InetAddressValidator.getInstance();
        return validator.isValidInet4Address(ipAddress);
    }

    private boolean isCardNumberValid(String number) {
        return LuhnCheckDigit.LUHN_CHECK_DIGIT.isValid(number);
    }
}