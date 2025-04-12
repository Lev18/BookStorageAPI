package com.bookstore.service.exception;

public class NoSuchAlgorithmForHash extends RuntimeException {
    public NoSuchAlgorithmForHash(String message) {
        super(message);
    }
}
