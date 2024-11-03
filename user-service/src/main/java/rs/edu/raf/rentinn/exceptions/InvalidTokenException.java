package rs.edu.raf.rentinn.exceptions;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("You are not allowed to take this action");
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
