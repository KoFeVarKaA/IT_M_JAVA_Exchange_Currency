package exchangecurrency.entity;

import java.math.BigDecimal;

public record Rate(
        long id,
        long baseCurrencyId,
        long targetCurrencyId,
        BigDecimal rate
) {}
