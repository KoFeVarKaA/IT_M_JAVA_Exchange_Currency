package exchangecurrency.dto.request;

public record RequestGetRateDto(
        String baseCurrencyId,
        String targetCurrencyId
) {}
