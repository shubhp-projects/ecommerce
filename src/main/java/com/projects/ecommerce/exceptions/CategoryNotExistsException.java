package com.projects.ecommerce.exceptions;

public class CategoryNotExistsException extends IllegalArgumentException {
    public CategoryNotExistsException(String msg) {
        super(msg);
    }
}
