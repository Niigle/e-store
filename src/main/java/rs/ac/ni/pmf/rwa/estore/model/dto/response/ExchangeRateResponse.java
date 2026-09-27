package rs.ac.ni.pmf.rwa.estore.model.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor()
public class ExchangeRateResponse {

    private Long id;
    private String currencyFrom;
    private String currencyTo;
    private BigDecimal exchangeRate;
    private LocalDate dateOf;
}
