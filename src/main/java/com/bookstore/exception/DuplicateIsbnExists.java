package com.bookstore.exception;

import org.springframework.stereotype.Component;

public class DuplicateIsbnExists extends RuntimeException {
    public DuplicateIsbnExists(String message) {
        super(message);
    }
}
