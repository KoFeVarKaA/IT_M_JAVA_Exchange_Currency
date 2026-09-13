package exchangecurrency.dto.request;

public record RequestGetRateDto(
        String baseCurrencyCode,
        String targetCurrencyCode
) {}
