package rs.ac.ni.pmf.rwa.estore.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.ni.pmf.rwa.estore.exception.DuplicateResourceException;
import rs.ac.ni.pmf.rwa.estore.exception.InvalidOperationException;
import rs.ac.ni.pmf.rwa.estore.exception.ResourceNotFoundException;
import rs.ac.ni.pmf.rwa.estore.model.dto.ErrorDto;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleIllegalArgumentException(final IllegalArgumentException ex) {
        log.warn("Illegal argument: {}", ex.getMessage());

        return ErrorDto.builder()
                .timestamp(OffsetDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        log.warn("Validation failed for request: {}", errors);

        return ErrorDto.builder()
                .timestamp(OffsetDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Validation failed for one or more fields.")
                .validationErrors(errors)
                .build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleResourceNotFoundException(final ResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());

        return ErrorDto.builder()
                .timestamp(OffsetDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handleDuplicateResourceException(final DuplicateResourceException ex) {
        log.warn("Duplicate resource: {}", ex.getMessage());

        return ErrorDto.builder()
                .timestamp(OffsetDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(InvalidOperationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorDto handleInvalidOperationException(final InvalidOperationException ex)
    {
        log.warn("Invalid operation: {}", ex.getMessage());

        return ErrorDto.builder()
                .timestamp(OffsetDateTime.now())
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDto> handleResponseStatusException(final ResponseStatusException ex) {
        log.warn("ResponseStatusException [{}]: {}", ex.getStatusCode(), ex.getReason());

        ErrorDto error = ErrorDto.builder()
                .timestamp(OffsetDateTime.now())
                .status(ex.getStatusCode().value())
                .error(ex.getReason())
                .message(ex.getReason())
                .build();

        return ResponseEntity.status(ex.getStatusCode()).body(error);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handleGlobalException(final Exception ex) {
        log.error("Unhandled internal server error occurred: ", ex);

        return ErrorDto.builder()
                .timestamp(OffsetDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("An unexpected internal server error occurred.")
                .build();
    }
}
