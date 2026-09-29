package rs.ac.ni.pmf.rwa.estore.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.ni.pmf.rwa.estore.model.dto.request.ExchangeRateRequest;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.ExchangeRateResponse;
import rs.ac.ni.pmf.rwa.estore.model.entity.ExchangeRateEntity;
import rs.ac.ni.pmf.rwa.estore.service.ExchangeRateService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/exchange-rates")
@Tag(name = "ExchangeRate", description = "API to convert prices for international orders")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping
    public List<ExchangeRateResponse> getAllExchangeRates(@SortDefault(sort = "id", direction = Sort.Direction.ASC) Sort sort) {

        return exchangeRateService.getAllExchangeRates(sort);
    }

    @GetMapping("/{id}")
    public ExchangeRateResponse getExchangeRateById(@PathVariable Long id) {
        return exchangeRateService.getExchangeRateById(id);
    }

    @GetMapping("/search")
    public Page<ExchangeRateResponse> getByCurrencies(
            @RequestParam String currencyFrom,
            @RequestParam String currencyTo,
            @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable) {

        return exchangeRateService.getExchangeRatesByCurrencies(currencyFrom, currencyTo, pageable);
    }

    @GetMapping("/by-date")
    public List<ExchangeRateResponse> getByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        return exchangeRateService.getExchangeRatesByDate(date);
    }

    @PostMapping
    public ExchangeRateResponse createExchangeRate(@RequestBody @Valid ExchangeRateRequest exchangeRateRequest) {

        return exchangeRateService.createExchangeRate(exchangeRateRequest);
    }

    @PutMapping("/{id}")
    public ExchangeRateResponse updateExchangeRate(@PathVariable Long id, @RequestBody @Valid ExchangeRateRequest exchangeRateRequest) {

        return exchangeRateService.updateExchangeRate(id, exchangeRateRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExchangeRate(@PathVariable Long id) {

        exchangeRateService.deleteExchangeRate(id);

    }
}