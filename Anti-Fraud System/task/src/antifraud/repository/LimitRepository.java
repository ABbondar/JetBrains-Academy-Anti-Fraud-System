package antifraud.repository;

import antifraud.model.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LimitRepository extends JpaRepository<Limit, Long> {
    Limit findLimitByCardNumber(String number);
}