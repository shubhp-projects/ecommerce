package com.projects.ecommerce.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(value = CategoryNotExistsException.class)
    public final ResponseEntity<String> handleCategoryNotExistsException(CategoryNotExistsException categoryNotExistsException) {
        return new ResponseEntity<>(categoryNotExistsException.getMessage(), HttpStatus.BAD_REQUEST);
    }
}