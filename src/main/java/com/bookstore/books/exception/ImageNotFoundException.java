package com.bookstore.books.exception;

public class ImageNotFound extends RuntimeException {
    public ImageNotFound(String message) {
        super(message);
    }
}
