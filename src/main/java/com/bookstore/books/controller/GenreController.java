package com.bookstore.books.controller;

import com.bookstore.books.criteria.SearchCriteria;
import com.bookstore.books.dto.requestDto.GenreRequestDto;
import com.bookstore.books.dto.responseDto.*;
import com.bookstore.books.entity.Genre;
import com.bookstore.books.service.BookService;
import com.bookstore.books.service.GenreService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
@AllArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN') or (hasRole('ADMIN')" +
            " and hasAuthority('CAN_UPDATE_BOOK'))")
    public ResponseEntity<GenreResponseDto> createGenre(@RequestBody
                                                           @Valid GenreRequestDto genreDto) {
        return new ResponseEntity<>(genreService.createGenre(genreDto), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<GenreWithBookDto>> getTopFiveGenresFiveBooks() {
        List<GenreWithBookDto> allBooksByGenre= genreService.getTopFiveGenresFiveBooks();
        return ResponseEntity.ok().body(allBooksByGenre);
    }

    @GetMapping("/list")
    public ResponseEntity<PageResponseDto<GenreInfoDTO>> getGenreList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setPage(page);
        searchCriteria.setSize(size);
        Pageable pageable = PageRequest.of(page, size);

        return genreService.getGenreList(searchCriteria,
                        pageable)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/{genre}")
    public ResponseEntity<PageResponseDto<GenreWithBookDto>> getAllBooksByGenre(
            @PathVariable String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setPage(page);
        searchCriteria.setSize(size);
        Pageable pageable = PageRequest.of(page, size);
        return genreService.getGetGenresBook(genre,
                        searchCriteria,
                        pageable)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('SUPER_ADMIN') or (hasRole('ADMIN')" +
            " and hasAuthority('CAN_DELETE_BOOK'))")
    @PutMapping("/{id}")
    GenreResponseDto updateGenre(@PathVariable(name = "id") Long id,
            @RequestBody GenreRequestDto dto) {
        return genreService.updateGenre(id, dto);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN') or (hasRole('ADMIN')" +
            " and hasAuthority('CAN_DELETE_BOOK'))")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }
}