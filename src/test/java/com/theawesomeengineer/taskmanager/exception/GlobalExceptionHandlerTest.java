package com.theawesomeengineer.taskmanager.exception;

import com.theawesomeengineer.taskmanager.openapi.model.Error;
import com.theawesomeengineer.taskmanager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        TaskService mockService = Mockito.mock(TaskService.class);
    }

    @Test
    void testHandleEntityNotFoundException() {
        EntityNotFoundException ex = new EntityNotFoundException("Task not found");
        ResponseEntity<Error> response = handler.handleEntityNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Task not found", response.getBody().getDetails());
        assertEquals("Not Found", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void testHandleMethodArgumentNotValidException() {
        MethodArgumentNotValidException ex = Mockito.mock(MethodArgumentNotValidException.class);
        ResponseEntity<Error> response = handler.HandleMethodArgumentNotValidException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid input value", response.getBody().getDetails());
        assertEquals("Bad Request", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void testHandleDataAccessException() {
        DataAccessException ex = new DataAccessException("DB error") {
        };
        ResponseEntity<Error> response = handler.handleDbUpdateException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Database operation error", response.getBody().getDetails());
        assertEquals("Internal Server Error", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void testHandleGeneralException() {
        RuntimeException ex = new RuntimeException("Something went wrong");
        ResponseEntity<Error> response = handler.handleGeneralError(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Something went wrong", response.getBody().getDetails());
        assertEquals("Internal Server Error", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }
}
