package cz.cvut.fel.ear.projekt.config;

import cz.cvut.fel.ear.projekt.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * A global exception handler for REST controllers. This class intercepts specific exceptions
 * and maps them to appropriate HTTP response statuses and problem details, improving
 * uniformity and clarity in error responses.
 *
 * Exception handling is provided for:
 * - IllegalArgumentException: Maps to HTTP 400 Bad Request, indicating invalid arguments were provided.
 * - IllegalStateException: Maps to HTTP 409 Conflict, indicating a conflict in the application state.
 * - NotFoundException: Maps to HTTP 404 Not Found, indicating that a requested resource could not be found.
 *
 * Each handler constructs a standardized ProblemDetail response body with the appropriate status
 * and error details.
 */
@RestControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ProblemDetail> handleIllegalArgument(IllegalArgumentException ex) {
        ProblemDetail body = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<ProblemDetail> handleIllegalState(IllegalStateException ex) {
        ProblemDetail body = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<ProblemDetail> handleNotFound(NotFoundException ex) {
        ProblemDetail body = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
