package com.bookstore.service.exception;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class UnableParseFile extends RuntimeException {
    public UnableParseFile(String message) {
        super(message);
    }
}
