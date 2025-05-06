package com.bookstore.books.exception;

public class BookAlreadyExistException extends Throwable {
    public BookAlreadyExistException(String theCurrentBookAlreadyExist) {
        super(theCurrentBookAlreadyExist);
    }
}
