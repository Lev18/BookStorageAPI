package com.bookstore.controller;

import com.bookstore.entity.Book;
import com.bookstore.service.BookService;
import com.bookstore.service.BookUploadService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class BookUploadController {
    private final BookUploadService bookUploadService;
    private final BookService bookService;

    @GetMapping
    public String front() {
        return "Welcome";
    }

    @PostMapping(path = "/book/file", consumes = {"multipart/form-data"})
    @SneakyThrows
    public ResponseEntity<?> uploadBookStore(@RequestPart("file") MultipartFile file) {
        return bookUploadService.uploadAndSaveFile(file);
    }

    @PutMapping(path = "/updateRating/{book_id}/{newRate}")
    public ResponseEntity<?> updateBookRating(@PathVariable String book_id, @PathVariable String newRate) {
        Book updatedBook = bookService.updateBookRating(book_id, newRate);
        if (updatedBook == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Book not found\n");
        }
        return ResponseEntity.ok(updatedBook.getBookId() + " book's rating was updated successfully");
    }

    @PostMapping(path = "/upload/image")
    public ResponseEntity<?> uploadImg() {
        bookService.uploadImg();
        return ResponseEntity.ok(" images was updated successfully");
    }

}
