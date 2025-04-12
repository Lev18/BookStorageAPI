package com.bookstore.service.exception;

public class NoSuchFile extends RuntimeException {
    public NoSuchFile(String message) {
        super(message);
    }
}
