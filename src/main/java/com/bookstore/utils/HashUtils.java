package com.bookstore.utils;

import com.bookstore.service.exception.NoSuchAlgorithmForHash;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class HashUtils {

    public static String computeSHA256(MultipartFile file) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(file.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new NoSuchAlgorithmForHash("Unable to create hashed string: " + e.getMessage());
        }

    }

}
