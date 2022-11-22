package antifraud.service;

import antifraud.model.Feedback;

public interface LimitService {

    long getAllowedLimit(String cardNumber);

    long getManualLimit(String cardNumber);

    void updateLimit(Feedback request);
}