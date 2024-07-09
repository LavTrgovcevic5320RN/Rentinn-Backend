package rs.edu.raf.rentinn.exceptions;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
        super("You are not allowed to take this action");
    }

    public ForbiddenException(String message) {
        super(message);
    }
}

