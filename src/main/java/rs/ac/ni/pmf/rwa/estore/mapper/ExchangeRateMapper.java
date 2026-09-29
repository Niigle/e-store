package rs.ac.ni.pmf.rwa.estore.mapper;

import org.springframework.stereotype.Component;
import rs.ac.ni.pmf.rwa.estore.model.dto.request.ExchangeRateRequest;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.ExchangeRateResponse;
import rs.ac.ni.pmf.rwa.estore.model.entity.ExchangeRateEntity;

@Component
public class ExchangeRateMapper {

    public ExchangeRateResponse toResponse(ExchangeRateEntity exchangeRateEntity) {

        return ExchangeRateResponse.builder()
                .currencyFrom(exchangeRateEntity.getCurrencyFrom())
                .currencyTo(exchangeRateEntity.getCurrencyTo())
                .exchangeRate(exchangeRateEntity.getExchangeRate())
                .dateOf(exchangeRateEntity.getDateOf())
                .build();

    }

    public ExchangeRateEntity toEntity(ExchangeRateRequest exchangeRateDto) {

        return ExchangeRateEntity.builder()
                .currencyFrom(exchangeRateDto.getCurrencyFrom())
                .currencyTo(exchangeRateDto.getCurrencyTo())
                .exchangeRate(exchangeRateDto.getExchangeRate())
                .dateOf(exchangeRateDto.getDateOf())
                .build();
    }

}
