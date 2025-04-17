package com.bookstore.controller;


import com.bookstore.entity.Book;
import com.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class UpdateBookController {
    private final BookService bookService;

    @PutMapping(path = "/book/update/rating/{bookIsbn}/{newRate}")
    public ResponseEntity<?> updateBookRating(@PathVariable String bookIsbn, @PathVariable String newRate) {
        Book updatedBook = bookService.updateBookRating(bookIsbn, newRate);
        if (updatedBook == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Book not found\n");
        }
        return ResponseEntity.ok(updatedBook.getBookId() + " book's rating was updated successfully");
    }

}
