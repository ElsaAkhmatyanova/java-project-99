package hexlet.code.exception;

public class RestrictionException extends RuntimeException {

    public RestrictionException() {
    }

    public RestrictionException(String message) {
        super(message);
    }

    public RestrictionException(String message, Throwable cause) {
        super(message, cause);
    }
}
