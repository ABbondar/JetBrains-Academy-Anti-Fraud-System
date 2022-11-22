package antifraud.repository;

import antifraud.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<List<Transaction>> findAllByCardNumber(String cardNumber);

    List<Transaction> findByCardNumberAndDateBetween(
            String cardNumber, LocalDateTime dateStart, LocalDateTime dateEnd);
}