package exchangecurrency.exeptons;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException() {
        super("Объект не найден");
    }
    public ObjectNotFoundException(String message) {
        super(message);
    }
}
