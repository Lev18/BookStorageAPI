package com.bookstore.controller;

import com.bookstore.dto.responceDto.BookInfoDTO;
import com.bookstore.dto.responceDto.GenreInfoDTO;
import com.bookstore.entity.Genre;
import com.bookstore.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class DeleteBookController {
    private final BookService bookService;

    @DeleteMapping(path = "/book/delete/{bookIsbn}")
    public ResponseEntity<?> deleteBookByISBN(@PathVariable String bookIsbn) {
        BookInfoDTO bookInfoDTO = bookService.deleteBookByISBN(bookIsbn);
        if (bookInfoDTO == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok().body(bookInfoDTO);
    }

    @DeleteMapping(path = "/genre/delete/{genreName}")
    public ResponseEntity<?> deleteGenre(@PathVariable String genreName) {
        Genre genre = bookService.deleteGenre(genreName);
        if (genre == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok().body(genre.getGenreTitle() + " genre  deleted\n");
    }
}
