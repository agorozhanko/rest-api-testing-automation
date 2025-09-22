package lib.exception;

public class PasswordNotFoundException extends NullPointerException {
    public PasswordNotFoundException(String message) {
        super(message);
    }

    public PasswordNotFoundException() {
        super("Пароль не найден среди введённых паролей.");
    }
}
