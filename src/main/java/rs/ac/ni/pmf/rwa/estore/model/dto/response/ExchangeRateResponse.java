package rs.ac.ni.pmf.rwa.estore.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor()
@Schema(description = "Response payload containing exchange rate details")
public class ExchangeRateResponse {

    @Schema(description = "Unique exchange rate ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Source currency code (3-letter ISO code)", example = "EUR")
    private String currencyFrom;

    @Schema(description = "Target currency code (3-letter ISO code)", example = "RSD")
    private String currencyTo;

    @Schema(description = "Conversion rate value", example = "117.25")
    private BigDecimal exchangeRate;

    @Schema(description = "Date when the exchange rate is effective (YYYY-MM-DD)", example = "2026-03-30")
    private LocalDate dateOf;
}
