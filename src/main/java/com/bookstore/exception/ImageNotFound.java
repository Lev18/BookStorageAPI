package com.bookstore.exception;

public class ImageNotFound extends RuntimeException {
    public ImageNotFound(String message) {
        super(message);
    }
}
