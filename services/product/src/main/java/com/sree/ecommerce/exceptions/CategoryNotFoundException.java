package com.sree.ecommerce.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryNotFoundException extends RuntimeException{
    private final String message;
}
