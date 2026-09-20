package exchangecurrency.dto.response;

public record ResponseCurrencyDto(
        long id,
        String code,
        String name,
        String sign
) {
}
