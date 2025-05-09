package com.bookstore.books.controller;

import com.bookstore.books.dto.responseDto.GenreInfoDTO;
import com.bookstore.books.entity.Genre;
import com.bookstore.books.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genres")
@AllArgsConstructor
public class GenreController {
    private final BookService bookService;

    @DeleteMapping(path = "/genre/{genreName}")
    public ResponseEntity<?> deleteGenre(@PathVariable String genreName) {
        Genre genre = bookService.deleteGenre(genreName);
        if (genre == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok().body(genre.getName() + " genre  deleted\n");
    }
    // TODO: permisson with specific role
    // Spring expression language
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/{genre}")
    public ResponseEntity<List<GenreInfoDTO>> getAllBooksByGenre(@PathVariable String genre) {
        List<GenreInfoDTO> allBooksByGenre= bookService.getAllBooksByGenre(genre);
        return ResponseEntity.ok().body(allBooksByGenre);
    }
}