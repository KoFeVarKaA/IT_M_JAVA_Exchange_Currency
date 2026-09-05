package exchangecurrency.exeptons;

import jakarta.servlet.http.HttpServletResponse;

public class ValidationException extends BaseException {
    private static final int errorCode = HttpServletResponse.SC_BAD_REQUEST;

    public ValidationException(String message) {
        super(errorCode, message);
    }
}
