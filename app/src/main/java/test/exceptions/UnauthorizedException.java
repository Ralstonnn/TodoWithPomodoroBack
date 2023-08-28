package test.exceptions;

public class UnauthorizedException extends Exception {
    private static final String ERROR_MESSAGE = "Unauthorized";

    public UnauthorizedException() {
        super(ERROR_MESSAGE);
    }
}
