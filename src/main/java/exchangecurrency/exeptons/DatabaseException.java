package exchangecurrency.exeptons;

import jakarta.servlet.http.HttpServletResponse;

public class DatabaseException extends BaseException {
    private static final int errorCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

    public DatabaseException() {
        super(errorCode, "Ошибка базы данных");
    }
    public DatabaseException(String message) {
        super(errorCode, message);
    }
}
