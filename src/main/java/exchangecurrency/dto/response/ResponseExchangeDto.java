package exchangecurrency.dto.response;

import exchangecurrency.entity.Currency;

import java.math.BigDecimal;

public record ResponseExchangeDto (
        long id,
        Currency baseCurrency,
        Currency targetCurrency,
        BigDecimal rate,
        BigDecimal amount,
        BigDecimal convertedAmount
){
}
