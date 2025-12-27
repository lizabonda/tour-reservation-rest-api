package cz.cvut.fel.ear.projekt.exception;

/**
 * This exception is thrown to indicate that a specific entity or resource
 * was not found during the execution of the operation.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
