package exchangecurrency.exeptons;

public class DatabaseException extends RuntimeException{
    public DatabaseException() {
        super("Ошибка базы данных");
    }
    public DatabaseException(String message) {
        super(message);
    }
}
