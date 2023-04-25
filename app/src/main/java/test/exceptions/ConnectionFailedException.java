package test.exceptions;

public class ConnectionFailedException extends Exception {
    private static final String ERROR_MESSAGE = "Connection failed";

    public ConnectionFailedException() {
        super(ERROR_MESSAGE);
    }

    public ConnectionFailedException(String errorMessage) {
        super(errorMessage);
    }
}
