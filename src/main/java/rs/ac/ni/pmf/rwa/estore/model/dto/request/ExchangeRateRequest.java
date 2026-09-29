package rs.ac.ni.pmf.rwa.estore.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor()
@Getter
@Setter
@Schema(description = "Request payload for creating or updating an exchange rate")
public class ExchangeRateRequest {

    @NotBlank(message = "currencyFrom cannot be blank")
    @Schema(description = "Source currency code (3-letter ISO code)", example = "EUR", requiredMode = Schema.RequiredMode.REQUIRED)
    private String currencyFrom;

    @NotBlank(message = "currencyTo cannot be blank")
    @Schema(description = "Target currency code (3-letter ISO code)", example = "RSD", requiredMode = Schema.RequiredMode.REQUIRED)
    private String currencyTo;

    @NotNull(message = "Exchange rate cannot be null")
    @Schema(description = "Conversion rate value", example = "117.25", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal exchangeRate;

    @NotNull(message = "date Of rate cannot be null")
    @Schema(description = "Date when the exchange rate is effective (YYYY-MM-DD)", example = "2026-03-30", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate dateOf;

}
