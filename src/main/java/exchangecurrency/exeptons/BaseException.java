package exchangecurrency.exeptons;

public abstract  class BaseException extends RuntimeException {
    private final int errorCode;

    public BaseException(int errorCode, String message) {
        this.errorCode = errorCode;
        super(message);
    }

    public int getErrorCode() {
        return errorCode;
    }
}
