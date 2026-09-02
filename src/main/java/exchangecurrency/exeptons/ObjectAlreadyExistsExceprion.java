package exchangecurrency.exeptons;

public class ObjectAlreadyExistsExceprion extends RuntimeException {
    public ObjectAlreadyExistsExceprion() {
        super("Ошибка - объект уже существует");
    }

    public ObjectAlreadyExistsExceprion(String message) {
        super(message);
    }
}
