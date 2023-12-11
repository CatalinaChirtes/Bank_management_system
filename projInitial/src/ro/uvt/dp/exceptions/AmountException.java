package ro.uvt.dp.exceptions;

public class AmountException extends Exception {
    public AmountException() {
        super();
    }
    public AmountException(String message) {
        super(message);
    }
    public AmountException(String message, Throwable cause) {
        super(message, cause);
    }
}
