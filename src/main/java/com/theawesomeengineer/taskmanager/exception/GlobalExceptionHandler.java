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
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Error> handleEntityNotFoundException(EntityNotFoundException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Error> handleDbUpdateException(DataAccessException e) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleGeneralError(Exception e) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    private ResponseEntity<Error> buildErrorResponse(HttpStatus status, Exception e) {
        Error error = new Error()
                .message(status.getReasonPhrase())
                .details(e.getMessage())
                .timestamp(OffsetDateTime.now());
        return ResponseEntity.status(status).body(error);
    }
}
