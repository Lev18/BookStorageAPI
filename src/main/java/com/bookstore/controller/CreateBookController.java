package com.bookstore.controller;

import com.bookstore.entity.Book;
import com.bookstore.service.BookService;
import com.bookstore.service.BookUploadService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CreateBookController {
    private final BookUploadService bookUploadService;
    private final BookService bookService;

    @GetMapping
    public String front() {
        return "Welcome";
    }

    @PostMapping(path = "/books/file", consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadBookStore(@RequestPart("file") MultipartFile file) {
        int savedBooks = bookUploadService.uploadAndSaveFile(file);
        if (savedBooks == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(savedBooks + " new  books were saved\n");
    }

    @PostMapping (path = "/image/download")
    public ResponseEntity<?> uploadImg() throws InterruptedException {
        bookService.uploadImg();
        return ResponseEntity.ok(" images was downloaded successfully");
    }

}

