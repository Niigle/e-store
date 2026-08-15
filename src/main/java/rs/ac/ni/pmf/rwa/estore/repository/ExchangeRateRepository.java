package rs.ac.ni.pmf.rwa.estore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.ni.pmf.rwa.estore.model.entity.ExchangeRateEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRateEntity, Long> {

    List<ExchangeRateEntity> findByCurrencyFromAndCurrencyTo(String currencyFrom, String currencyTo);
    Optional<ExchangeRateEntity> findByCurrencyFromAndCurrencyToAndDateOf(String currencyFrom, String currencyTo, LocalDate dateOf);
    List<ExchangeRateEntity> findByDateOf(LocalDate dateOf);

}
