package com.theawesomeengineer.taskmanager.exception;

import com.theawesomeengineer.taskmanager.openapi.model.Error;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> handleEntityNotFoundException(MethodArgumentNotValidException e) {
        Error error = new Error()
                .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .details("Invalid input value")
                .timestamp(OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Error> handleEntityNotFoundException(EntityNotFoundException e) {
        Error error = new Error()
                .message(HttpStatus.NOT_FOUND.getReasonPhrase())
                .details(e.getMessage())
                .timestamp(OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Error> handleDbUpdateException(DataAccessException e) {
        Error error = new Error()
                .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .details("Database operation error")
                .timestamp(OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleGeneralError(Exception e) {
        Error error = new Error()
                .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .details(e.getMessage())
                .timestamp(OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
