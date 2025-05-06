package com.bookstore.books.utils;

import com.bookstore.books.exception.NoSuchAlgorithmForHashException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HashUtils {

    public static String computeSHA256(MultipartFile file) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(file.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new NoSuchAlgorithmForHashException("Unable to create hashed string: " + e.getMessage());
        }

    }

}
