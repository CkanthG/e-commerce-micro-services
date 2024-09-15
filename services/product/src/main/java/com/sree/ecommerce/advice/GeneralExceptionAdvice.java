package com.sree.ecommerce.advice;

import com.sree.ecommerce.exceptions.CategoryNotFoundException;
import com.sree.ecommerce.exceptions.ProductNotFoundException;
import com.sree.ecommerce.exceptions.ProductPurchaseException;
import com.sree.ecommerce.models.ErrorResponse;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GeneralExceptionAdvice {

    @ExceptionHandler({ProductNotFoundException.class, CategoryNotFoundException.class})
    public ProblemDetail handle(Exception ex) {
        return ProblemDetail.forStatusAndDetail(NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGlobalExceptions(Exception ex) {
        return ProblemDetail.forStatusAndDetail(INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(ProductPurchaseException.class)
    public ProblemDetail handle(ProductPurchaseException ex) {
        return ProblemDetail.forStatusAndDetail(BAD_REQUEST, ex.getMessage());
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
