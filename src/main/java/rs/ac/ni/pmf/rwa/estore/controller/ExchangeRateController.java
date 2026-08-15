package rs.ac.ni.pmf.rwa.estore.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.ni.pmf.rwa.estore.model.entity.ExchangeRateEntity;
import rs.ac.ni.pmf.rwa.estore.service.ExchangeRateService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/exchange-rates")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping
    public ResponseEntity<List<ExchangeRateEntity>> getAllExchangeRates() {
        return ResponseEntity.ok(exchangeRateService.getAllExchangeRates());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExchangeRateEntity> getExchangeRateById(@PathVariable Long id) {
        return exchangeRateService.getExchangeRateById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ExchangeRateEntity>> getByCurrencies(
            @RequestParam String currencyFrom,
            @RequestParam String currencyTo) {
        return ResponseEntity.ok(exchangeRateService.getExchangeRatesByCurrencies(currencyFrom, currencyTo));
    }

    @GetMapping("/by-date")
    public ResponseEntity<List<ExchangeRateEntity>> getByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(exchangeRateService.getExchangeRatesByDate(date));
    }

    @PostMapping
    public ResponseEntity<ExchangeRateEntity> createExchangeRate(@RequestBody ExchangeRateEntity exchangeRate) {
        ExchangeRateEntity created = exchangeRateService.createExchangeRate(exchangeRate);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExchangeRateEntity> updateExchangeRate(@PathVariable Long id, @RequestBody ExchangeRateEntity details) {
        try {
            ExchangeRateEntity updated = exchangeRateService.updateExchangeRate(id, details);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExchangeRate(@PathVariable Long id) {
        try {
            exchangeRateService.deleteExchangeRate(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}