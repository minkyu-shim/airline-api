package com.epita.airlineapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.NoSuchElementException;

// "Safety Net." It listens for the specific exceptions our services will throw.
@ControllerAdvice
public class GlobalExceptionHandler {

    // HANDLE 404 (Not Found)
    // Catches "NoSuchElementException" thrown by service
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoSuchElementException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage() // This message comes from your Service!
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // HANDLE 400 (Bad Request)
    // Catches "IllegalStateException"
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
