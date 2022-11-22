package antifraud.service.impl;

import antifraud.exception.transaction.TransactionNotFoundException;
import antifraud.model.Limit;
import antifraud.model.Feedback;
import antifraud.model.Transaction;
import antifraud.repository.LimitRepository;
import antifraud.repository.TransactionRepository;
import antifraud.service.LimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class LimitServiceImpl implements LimitService {

    private final LimitRepository limitRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public long getAllowedLimit(String cardNumber) {
        var c = limitRepository.findLimitByCardNumber(cardNumber);
        return c.getMaxAllowed();
    }

    @Override
    public long getManualLimit(String cardNumber) {
        var c = limitRepository.findLimitByCardNumber(cardNumber);
        return c.getMaxManual();
    }

    @Override
    public void updateLimit(Feedback request) {
        Transaction transaction = transactionRepository
                .findById(request.getTransactionId())
                .orElseThrow(TransactionNotFoundException::new);

        if (!transaction.getFeedback().equals("")) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        if (request.getFeedback().equals(transaction.getStatus())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (limitRepository.findAll().isEmpty()) {
            Limit l = new Limit();
            l.setCardNumber(transaction.getCardNumber());
            limitRepository.save(l);
        }

        Limit limit = limitRepository.findLimitByCardNumber(transaction.getCardNumber());

        if (transaction.getStatus()
                .equals("ALLOWED") && request.getFeedback()
                .equals("MANUAL_PROCESSING")) {

            decreaseAllowedLimit(limit, transaction.getAmount());

        } else if (transaction.getStatus()
                .equals("ALLOWED") && request.getFeedback()
                .equals("PROHIBITED")) {

            decreaseAllowedLimit(limit, transaction.getAmount());
            decreaseManualLimit(limit, transaction.getAmount());

        } else if (transaction.getStatus()
                .equals("MANUAL_PROCESSING") && request.getFeedback()
                .equals("ALLOWED")) {

            increaseAllowedLimit(limit, transaction.getAmount());

        } else if (transaction.getStatus()
                .equals("MANUAL_PROCESSING") && request.getFeedback()
                .equals("PROHIBITED")) {

            decreaseManualLimit(limit, transaction.getAmount());

        } else if (transaction.getStatus()
                .equals("PROHIBITED") && request.getFeedback()
                .equals("ALLOWED")) {

            increaseAllowedLimit(limit, transaction.getAmount());
            increaseManualLimit(limit, transaction.getAmount());

        } else if (transaction.getStatus()
                .equals("PROHIBITED") && request.getFeedback()
                .equals("MANUAL_PROCESSING")) {

            increaseManualLimit(limit, transaction.getAmount());
        }
        limitRepository.save(limit);
    }

    private void increaseAllowedLimit(Limit limit, long amount) {
        double l = Math.ceil(0.8 * limit.getMaxAllowed() + 0.2 * amount);
        limit.setMaxAllowed((long) l);
    }

    private void decreaseAllowedLimit(Limit limit, long amount) {
        double l = Math.ceil(0.8 * limit.getMaxAllowed() - 0.2 * amount);
        limit.setMaxAllowed((long) l);
    }

    private void increaseManualLimit(Limit limit, long amount) {
        double l = Math.ceil(0.8 * limit.getMaxManual() + 0.2 * amount);
        limit.setMaxManual((long) l);
    }

    private void decreaseManualLimit(Limit limit, long amount) {
        double l = Math.ceil(0.8 * limit.getMaxManual() - 0.2 * amount);
        limit.setMaxManual((long) l);
    }
}