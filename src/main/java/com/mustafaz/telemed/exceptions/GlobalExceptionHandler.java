package com.mustafaz.telemed.exceptions;

import com.mustafaz.telemed.res.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<?>> handleAllUnknownExceptions(Exception ex) {
        return handleExceptions(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response<?>> handleNotFoundExceptions(NotFoundException ex) {
        return handleExceptions(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Response<?>> handleBadRequestExceptionExceptions(BadRequestException ex) {
        return handleExceptions(ex, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Response<?>> handleExceptions(Exception ex, HttpStatus status) {
        Response<?> response = Response.builder()
                .statusCode(status.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, status);
    }
}
