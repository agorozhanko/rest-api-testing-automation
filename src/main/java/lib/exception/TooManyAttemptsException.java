package lib.exception;

public class TooManyAttemptsException extends RuntimeException {
    public TooManyAttemptsException(String message) {
        super(message);
    }
}