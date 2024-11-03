package rs.edu.raf.rentinn.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("You are not allowed to take this action");
    }

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
