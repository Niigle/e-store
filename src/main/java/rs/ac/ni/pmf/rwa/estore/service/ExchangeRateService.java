package rs.ac.ni.pmf.rwa.estore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;import org.springframework.stereotype.Service;
import rs.ac.ni.pmf.rwa.estore.exception.ResourceNotFoundException;
import rs.ac.ni.pmf.rwa.estore.mapper.ExchangeRateMapper;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.ExchangeRateResponse;
import rs.ac.ni.pmf.rwa.estore.model.entity.ExchangeRateEntity;
import rs.ac.ni.pmf.rwa.estore.repository.ExchangeRateRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateMapper exchangeRateMapper;

    public List<ExchangeRateResponse> getAllExchangeRates(Sort sort) {
        return exchangeRateRepository.findAll(sort).stream().map(exchangeRateMapper::toResponse).toList();
    }

    public ExchangeRateResponse getExchangeRateById(Long id) {
        return exchangeRateRepository.findById(id).map(exchangeRateMapper::toResponse).orElseThrow(() -> new ResourceNotFoundException("Exchange rate not found with id: " + id));
    }

    public Page<ExchangeRateResponse> getExchangeRatesByCurrencies(String currencyFrom, String currencyTo, Pageable pageable) {

        return exchangeRateRepository.findByCurrencyFromAndCurrencyTo(currencyFrom, currencyTo, pageable).map(exchangeRateMapper::toResponse);
    }

    public List<ExchangeRateResponse> getExchangeRatesByDate(LocalDate dateOf) {
        return exchangeRateRepository.findByDateOf(dateOf).stream().map(exchangeRateMapper::toResponse).toList();
    }

    public ExchangeRateResponse createExchangeRate(ExchangeRateEntity exchangeRate) {
        return exchangeRateMapper.toResponse(exchangeRateRepository.save(exchangeRate));
    }

    public ExchangeRateResponse updateExchangeRate(Long id, ExchangeRateEntity details) {
        ExchangeRateEntity exchangeRate = exchangeRateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exchange rate with id " + id + " not found"));

        exchangeRate.setCurrencyFrom(details.getCurrencyFrom());
        exchangeRate.setCurrencyTo(details.getCurrencyTo());
        exchangeRate.setExchangeRate(details.getExchangeRate());
        exchangeRate.setDateOf(details.getDateOf());

        return exchangeRateMapper.toResponse(exchangeRateRepository.save(exchangeRate));
    }

    public void deleteExchangeRate(Long id) {

        ExchangeRateEntity exchangeRate = exchangeRateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exchange rate with id " + id + " not found"));

        exchangeRateRepository.delete(exchangeRate);
    }

}