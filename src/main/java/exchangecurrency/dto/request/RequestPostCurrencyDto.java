package exchangecurrency.dto.request;

public record RequestPostCurrencyDto(
        String code,
        String fullName,
        String sign
){
}
