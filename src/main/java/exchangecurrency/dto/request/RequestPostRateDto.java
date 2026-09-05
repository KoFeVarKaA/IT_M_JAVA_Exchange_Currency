package exchangecurrency.dto.request;

import java.math.BigDecimal;

public record RequestPostRateDto(
        long baseCurrencyId,
        long targetCurrencyId,
        BigDecimal rate
) {}
