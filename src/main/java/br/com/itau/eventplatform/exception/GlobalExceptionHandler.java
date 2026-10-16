package br.com.itau.eventplatform.exception;

import br.com.itau.eventplatform.api.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex) {

        ErrorResponse response =
                new ErrorResponse(
                        "VALIDATION_ERROR",
                        ex.getBindingResult()
                                .getFieldError()
                                .getDefaultMessage(),
                        LocalDateTime.now());

        return ResponseEntity.badRequest()
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex) {

        ErrorResponse response =
                new ErrorResponse(
                        "INTERNAL_ERROR",
                        ex.getMessage(),
                        LocalDateTime.now());

        return ResponseEntity.status(
                        HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}