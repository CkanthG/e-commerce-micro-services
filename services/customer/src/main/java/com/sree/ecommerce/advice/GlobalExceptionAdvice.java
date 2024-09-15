package com.sree.ecommerce.advice;

import com.sree.ecommerce.exceptions.CustomerNotFoundException;
import com.sree.ecommerce.models.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ProblemDetail handle(CustomerNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException ex) {
        var errors = new HashMap<String, String>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
                            var field = error.getField();
                            var message = error.getDefaultMessage();
                            errors.put(field, message);
                        }
                );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(errors));
    }
}
