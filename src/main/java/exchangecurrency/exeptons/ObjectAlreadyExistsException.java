package exchangecurrency.exeptons;

import jakarta.servlet.http.HttpServletResponse;

public class ObjectAlreadyExistsException extends BaseException {
    private static final int errorCode = HttpServletResponse.SC_CONFLICT;

    public ObjectAlreadyExistsException() {
        super(errorCode, "Ошибка - объект уже существует");
    }

    public ObjectAlreadyExistsException(String message) {
        super(errorCode, message);
    }
}
