package com.bookstore.controller;

import com.bookstore.entity.Book;
import com.bookstore.service.BookService;
import com.bookstore.service.BookUploadService;
import com.bookstore.service.file_reader.CsvReaderService;
import com.bookstore.service.dto.BookCsvDto;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")

//TODO change class name
// refactor uploadBookStore method
@AllArgsConstructor
public class ApplicationController {
    private final CsvReaderService csvReaderService;
    private final BookUploadService bookUploadService;
    private final BookService bookService;

    @GetMapping
    public String front() {
        return "Welcome";
    }

    // books/file
    @PostMapping(path = "/book/file", consumes = {"multipart/form-data"})
    @SneakyThrows
    public ResponseEntity<?> uploadBookStore(@RequestPart("file") MultipartFile file) {
        //TODO move to BookUploadService class
        List<BookCsvDto> csvDTos = csvReaderService.uploadBooks(file);
        if (csvDTos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("File already parsed NO new book");
        }
        int savedBook = bookUploadService.saveBook(csvDTos);
        return ResponseEntity.ok(savedBook + " new book were added\n");
    }

    @PutMapping(path = "/updateRating/{book_id}/{newRate}")
    public ResponseEntity<?> updateBookRating(@PathVariable String book_id, @PathVariable String newRate) {
        Book updatedBook = bookService.updateBookRating(book_id, newRate);
        if (updatedBook == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Book not found\n");
        }
        return ResponseEntity.ok(updatedBook.getBookId() + " book's rating was updated successfully");
    }

    @PostMapping(path = "/upload_image")
    public ResponseEntity<?> uploadImg() {
        bookService.uploadImg();
        return ResponseEntity.ok(" images was updated successfully");
    }

}
