package com.bookstore.exception;

public class NoSuchFile extends RuntimeException {
    public NoSuchFile(String message) {
        super(message);
    }
}
