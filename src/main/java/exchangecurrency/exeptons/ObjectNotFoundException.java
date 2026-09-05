package exchangecurrency.exeptons;

import jakarta.servlet.http.HttpServletResponse;

public class ObjectNotFoundException extends BaseException {
    private static final int errorCode = HttpServletResponse.SC_NOT_FOUND;

    public ObjectNotFoundException() {
        super(errorCode, "Объект не найден");
    }
    public ObjectNotFoundException(String message) {
        super(errorCode, message);
    }
}
