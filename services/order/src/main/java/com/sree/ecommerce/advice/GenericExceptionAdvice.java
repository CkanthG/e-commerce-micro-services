package com.sree.ecommerce.advice;

import com.sree.ecommerce.exceptions.BusinessException;
import com.sree.ecommerce.exceptions.OrderNotFoundException;
import com.sree.ecommerce.models.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GenericExceptionAdvice {

    @ExceptionHandler({BusinessException.class, OrderNotFoundException.class})
    public ProblemDetail handle(Exception ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleAllExceptions(Exception ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
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
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(errors));
    }
}
