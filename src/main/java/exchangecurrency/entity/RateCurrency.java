package exchangecurrency.entity;

import java.math.BigDecimal;

public record RateCurrency(
        long id,
        Currency baseCurrency,
        Currency targetCurrency,
        BigDecimal rate
) {}
