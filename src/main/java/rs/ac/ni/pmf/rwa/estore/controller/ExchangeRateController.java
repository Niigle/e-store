package rs.ac.ni.pmf.rwa.estore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rs.ac.ni.pmf.rwa.estore.model.dto.request.ExchangeRateRequest;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.ExchangeRateResponse;
import rs.ac.ni.pmf.rwa.estore.service.ExchangeRateService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/exchange-rates")
@RequiredArgsConstructor
@Tag(name = "Exchange Rates", description = "Exchange rate management and price conversion API")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @Operation(summary = "Get all exchange rates", description = "Retrieve a sorted list of all exchange rates")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of exchange rates")
    @GetMapping
    public List<ExchangeRateResponse> getAllExchangeRates(
            @Parameter(description = "Sorting parameters")
            @SortDefault(sort = "id", direction = Sort.Direction.ASC) Sort sort) {

        return exchangeRateService.getAllExchangeRates(sort);
    }

    @Operation(summary = "Get exchange rate by ID", description = "Retrieve a single exchange rate by its unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exchange rate found",
                    content = @Content(schema = @Schema(implementation = ExchangeRateResponse.class))),
            @ApiResponse(responseCode = "404", description = "Exchange rate with the given ID does not exist", content = @Content)
    })
    @GetMapping("/{id}")
    public ExchangeRateResponse getExchangeRateById(
            @Parameter(description = "Exchange Rate ID", example = "1") @PathVariable Long id) {

        return exchangeRateService.getExchangeRateById(id);
    }

    @Operation(summary = "Search exchange rates by currencies", description = "Paginated search for exchange rates between two currencies")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved filtered list of exchange rates")
    @GetMapping("/search")
    public Page<ExchangeRateResponse> getByCurrencies(
            @Parameter(description = "Source currency code", example = "EUR") @RequestParam String currencyFrom,
            @Parameter(description = "Target currency code", example = "RSD") @RequestParam String currencyTo,
            @Parameter(description = "Pagination and sorting parameters")
            @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable) {

        return exchangeRateService.getExchangeRatesByCurrencies(currencyFrom, currencyTo, pageable);
    }

    @Operation(summary = "Get exchange rates by date", description = "Retrieve all exchange rates recorded on a specific date")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved exchange rates for the given date")
    @GetMapping("/by-date")
    public List<ExchangeRateResponse> getByDate(
            @Parameter(description = "Date in ISO format (YYYY-MM-DD)", example = "2026-03-30")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        return exchangeRateService.getExchangeRatesByDate(date);
    }

    @Operation(summary = "Create a new exchange rate", description = "Create an exchange rate entry with the provided details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exchange rate created successfully",
                    content = @Content(schema = @Schema(implementation = ExchangeRateResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload", content = @Content)
    })
    @PostMapping
    public ExchangeRateResponse createExchangeRate(
            @RequestBody @Valid ExchangeRateRequest exchangeRateRequest) {

        return exchangeRateService.createExchangeRate(exchangeRateRequest);
    }

    @Operation(summary = "Update an existing exchange rate", description = "Update exchange rate details by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exchange rate updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload", content = @Content),
            @ApiResponse(responseCode = "404", description = "Exchange rate with the given ID does not exist", content = @Content)
    })
    @PutMapping("/{id}")
    public ExchangeRateResponse updateExchangeRate(
            @Parameter(description = "Exchange Rate ID", example = "1") @PathVariable Long id,
            @RequestBody @Valid ExchangeRateRequest exchangeRateRequest) {

        return exchangeRateService.updateExchangeRate(id, exchangeRateRequest);
    }

    @Operation(summary = "Delete exchange rate", description = "Remove an exchange rate entry by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Exchange rate deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Exchange rate with the given ID does not exist", content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExchangeRate(
            @Parameter(description = "Exchange Rate ID", example = "1") @PathVariable Long id) {

        exchangeRateService.deleteExchangeRate(id);
    }
}