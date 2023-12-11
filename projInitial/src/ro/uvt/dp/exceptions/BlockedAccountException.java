package ro.uvt.dp.exceptions;

public class BlockedAccountException extends Exception {
    public BlockedAccountException() {
        super();
    }
    public BlockedAccountException(String message) {
        super(message);
    }
    public BlockedAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
