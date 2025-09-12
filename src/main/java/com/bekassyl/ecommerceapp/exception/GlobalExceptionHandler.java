package com.bekassyl.ecommerceapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails handlingMethodArgumentNotValidExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (msg1, msg2) -> msg1
                ));

        return new ErrorDetails(errors.toString(), LocalDateTime.now());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails handlingBadCredentialsExceptions(BadCredentialsException ex) {
        return new ErrorDetails(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(FileStorageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails handlingFileStorageExceptions(FileStorageException ex) {
        return new ErrorDetails(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDetails handlingResourceNotFoundExceptions(ResourceNotFoundException ex) {
        return new ErrorDetails(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDetails handlingUsernameNotFoundExceptions(UsernameNotFoundException ex) {
        return new ErrorDetails(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDetails> handlingResponseStatusExceptions(ResponseStatusException ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(new ErrorDetails(ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(InsufficientStockException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails handlingInsufficientStockExceptions(InsufficientStockException ex) {
        return new ErrorDetails(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails handlingIllegalStateExceptions(IllegalStateException ex) {
        return new ErrorDetails(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(EmailNotConfirmedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDetails handlingEmailNotConfirmedExceptions(EmailNotConfirmedException ex) {
        return new ErrorDetails(ex.getMessage(), LocalDateTime.now());
    }
}
