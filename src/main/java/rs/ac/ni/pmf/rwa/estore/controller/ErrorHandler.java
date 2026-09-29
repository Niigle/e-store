package rs.ac.ni.pmf.rwa.estore.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex)
    {
        log.error("Invalid input at processing request: {}", ex.getMessage());

        return ErrorDto.builder()
                .timestamp(OffsetDateTime.now())
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleResourceNotFoundException(final ResourceNotFoundException ex)
    {
        log.error("Resource not found: {}", ex.getMessage());

        return ErrorDto.builder()
                .timestamp(OffsetDateTime.now())
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handleDuplicateResourceException(final DuplicateResourceException ex)
    {
        log.warn("Duplicate resource found: {}", ex.getMessage());

        return ErrorDto.builder()
                .timestamp(OffsetDateTime.now())
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
    public ResponseEntity<ErrorDto> handleResponseStatusException(final ResponseStatusException ex)
    {
        log.warn("ResponseStatusException: {}", ex.getMessage());

        final ErrorDto error =  ErrorDto.builder()
                .timestamp(OffsetDateTime.now())
                .message(ex.getReason())
                .build();

        return ResponseEntity.status(ex.getStatusCode()).body(error);
    }


    //TODO
/*    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGlobalException(
            Exception ex, WebRequest request) {

        final ErrorDto error =  ErrorDto.builder()
                .timestamp(OffsetDateTime.now())
                .message("Unexpected server error.")
                .build();

        log.error("Unexpected server error: {}", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }*/
}
