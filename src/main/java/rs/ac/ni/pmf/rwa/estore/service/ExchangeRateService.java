package rs.ac.ni.pmf.rwa.estore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;import org.springframework.stereotype.Service;
import rs.ac.ni.pmf.rwa.estore.exception.ResourceNotFoundException;
import rs.ac.ni.pmf.rwa.estore.mapper.ExchangeRateMapper;
import rs.ac.ni.pmf.rwa.estore.model.dto.request.ExchangeRateRequest;
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

    @Cacheable(value = "exchangeRatesList", key = "#sort.toString()")
    public List<ExchangeRateResponse> getAllExchangeRates(Sort sort) {
        return exchangeRateRepository.findAll(sort).stream().map(exchangeRateMapper::toResponse).toList();
    }

    @Cacheable(value = "exchangeRatesById", key = "#id")
    public ExchangeRateResponse getExchangeRateById(Long id) {
        return exchangeRateRepository.findById(id).map(exchangeRateMapper::toResponse).orElseThrow(() -> new ResourceNotFoundException("Exchange rate not found with id: " + id));
    }

    @Cacheable(value = "exchangeRates", key = "#currencyFrom + '_' + #currencyTo + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<ExchangeRateResponse> getExchangeRatesByCurrencies(String currencyFrom, String currencyTo, Pageable pageable) {

        return exchangeRateRepository.findByCurrencyFromAndCurrencyTo(currencyFrom, currencyTo, pageable).map(exchangeRateMapper::toResponse);
    }

    @Cacheable(value = "exchangeRatesDate", key = "#dateOf")
    public List<ExchangeRateResponse> getExchangeRatesByDate(LocalDate dateOf) {
        return exchangeRateRepository.findByDateOf(dateOf).stream().map(exchangeRateMapper::toResponse).toList();
    }

    @Caching(evict = {
            @CacheEvict(value = "exchangeRatesById", allEntries = true),
            @CacheEvict(value = "exchangeRatesList", allEntries = true),
            @CacheEvict(value = "exchangeRates", allEntries = true),
            @CacheEvict(value = "exchangeRatesDate", allEntries = true)
    })
    public ExchangeRateResponse createExchangeRate(ExchangeRateRequest exchangeRateRequest) {

        return exchangeRateMapper.toResponse(exchangeRateRepository.save(exchangeRateMapper.toEntity(exchangeRateRequest)));
    }

    @Caching(
            put = @CachePut(value = "exchangeRatesById", key = "#id"),
            evict = {
                    @CacheEvict(value = "exchangeRatesList", allEntries = true),
                    @CacheEvict(value = "exchangeRates", allEntries = true),
                    @CacheEvict(value = "exchangeRatesDate", allEntries = true)
            }
    )
    public ExchangeRateResponse updateExchangeRate(Long id, ExchangeRateRequest exchangeRateRequest) {
        ExchangeRateEntity exchangeRate = exchangeRateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exchange rate with id " + id + " not found"));

        exchangeRate.setCurrencyFrom(exchangeRateRequest.getCurrencyFrom());
        exchangeRate.setCurrencyTo(exchangeRateRequest.getCurrencyTo());
        exchangeRate.setExchangeRate(exchangeRateRequest.getExchangeRate());
        exchangeRate.setDateOf(exchangeRateRequest.getDateOf());

        return exchangeRateMapper.toResponse(exchangeRateRepository.save(exchangeRateMapper.toEntity(exchangeRateRequest)));
    }

    @Caching(evict = {
            @CacheEvict(value = "exchangeRatesById", key = "#id"),
            @CacheEvict(value = "exchangeRatesList", allEntries = true),
            @CacheEvict(value = "exchangeRates", allEntries = true),
            @CacheEvict(value = "exchangeRatesDate", allEntries = true)
    })
    public void deleteExchangeRate(Long id) {

        ExchangeRateEntity exchangeRate = exchangeRateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exchange rate with id " + id + " not found"));

        exchangeRateRepository.delete(exchangeRate);
    }

}