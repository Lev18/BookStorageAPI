package com.bookstore.books.exception;

public class NoSuchFileException extends RuntimeException {
    public NoSuchFileException(String message) {
        super(message);
    }
}
