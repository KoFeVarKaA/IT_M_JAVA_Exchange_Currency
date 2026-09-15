package exchangecurrency.dto.request;

import java.math.BigDecimal;

public record RequestPatchRateDto(
        String baseCurrencyCode,
        String targetCurrencyCode,
        BigDecimal rate
) {}
