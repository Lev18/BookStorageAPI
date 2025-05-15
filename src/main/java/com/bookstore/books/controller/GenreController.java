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

    @PreAuthorize("hasRole('SUPER_ADMIN') or (hasRole('ADMIN')" +
            " and hasAuthority('CAN_DELETE_BOOK'))")    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        bookService.deleteGenre(id);
       return ResponseEntity.noContent().build();
    }
    @GetMapping(path = "/{genre}")
    public ResponseEntity<List<GenreInfoDTO>> getAllBooksByGenre(@PathVariable String genre) {
        List<GenreInfoDTO> allBooksByGenre= bookService.getAllBooksByGenre(genre);
        return ResponseEntity.ok().body(allBooksByGenre);
    }
}