package rs.ac.ni.pmf.rwa.estore.service;

import org.springframework.stereotype.Service;
import rs.ac.ni.pmf.rwa.estore.model.entity.ExchangeRateEntity;
import rs.ac.ni.pmf.rwa.estore.repository.ExchangeRateRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;

    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    public List<ExchangeRateEntity> getAllExchangeRates() {
        return exchangeRateRepository.findAll();
    }

    public Optional<ExchangeRateEntity> getExchangeRateById(Long id) {
        return exchangeRateRepository.findById(id);
    }

    public List<ExchangeRateEntity> getExchangeRatesByCurrencies(String currencyFrom, String currencyTo) {
        return exchangeRateRepository.findByCurrencyFromAndCurrencyTo(currencyFrom, currencyTo);
    }

    public List<ExchangeRateEntity> getExchangeRatesByDate(LocalDate dateOf) {
        return exchangeRateRepository.findByDateOf(dateOf);
    }

    public ExchangeRateEntity createExchangeRate(ExchangeRateEntity exchangeRate) {
        return exchangeRateRepository.save(exchangeRate);
    }

    public ExchangeRateEntity updateExchangeRate(Long id, ExchangeRateEntity details) {
        ExchangeRateEntity exchangeRate = exchangeRateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kurs sa id " + id + " nije pronađen"));

        exchangeRate.setCurrencyFrom(details.getCurrencyFrom());
        exchangeRate.setCurrencyTo(details.getCurrencyTo());
        exchangeRate.setExchangeRate(details.getExchangeRate());
        exchangeRate.setDateOf(details.getDateOf());

        return exchangeRateRepository.save(exchangeRate);
    }

    public void deleteExchangeRate(Long id) {
        ExchangeRateEntity exchangeRate = exchangeRateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kurs sa id " + id + " nije pronađen"));
        exchangeRateRepository.delete(exchangeRate);
    }
}