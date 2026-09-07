package exchangecurrency.dto.response;

public record ResponseErrorDto (
        int status,
        String message
) {
}
