package exchangecurrency.dto.response;

import java.math.BigDecimal;

public record ResponseRateDto (
        long id,
        long baseCurrencyId,
        long targetCurrencyId,
        BigDecimal rate
) {}

