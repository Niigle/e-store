package rs.ac.ni.pmf.rwa.estore.model.dto.request;

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
public class ExchangeRateRequest {

    @NotBlank(message = "currencyFrom cannot be blank")
    private String currencyFrom;

    @NotBlank(message = "currencyTo cannot be blank")
    private String currencyTo;

    @NotNull(message = "Exchange rate cannot be null")
    private BigDecimal exchangeRate;

    @NotNull(message = "date Of rate cannot be null")
    private LocalDate dateOf;

}
