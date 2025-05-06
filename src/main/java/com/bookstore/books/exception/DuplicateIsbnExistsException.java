package com.bookstore.books.exception;

public class DuplicateIsbnExistsException extends RuntimeException {
    public DuplicateIsbnExistsException(String message) {
        super(message);
    }
}
