package com.bookstore.books.controller;

import com.bookstore.books.criteria.SearchCriteria;
import com.bookstore.books.dto.requestDto.CharacterRequestDto;
import com.bookstore.books.dto.requestDto.GenreRequestDto;
import com.bookstore.books.dto.responseDto.*;
import com.bookstore.books.entity.Characters;
import com.bookstore.books.service.CharacterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/characters")
@RequiredArgsConstructor
public class CharacterController {
    private final CharacterService characterService;

    @PreAuthorize("hasRole('SUPER_ADMIN') or (hasRole('ADMIN') " +
            " and hasAuthority('CAN_INSERT_BOOK'))")
    @PostMapping
    public ResponseEntity<CharacterResponseDto> insertNewCharacter(@RequestBody
                                                                   @Valid CharacterRequestDto dto) {
        return  new ResponseEntity<>(characterService.insertNewCharacter(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<CharacterResponseDto>> getAllCharacters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setPage(page);
        searchCriteria.setSize(size);
        Pageable pageable = PageRequest.of(page, size);

        return characterService.getAllCharacters(searchCriteria,
                        pageable)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CharacterWithBookDto> getCharacterWithBooks(
                    @PathVariable(name = "id") Long id) {
        return characterService.getCharacterWithBooks(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('SUPER_ADMIN') or (hasRole('ADMIN')" +
            " and hasAuthority('CAN_UPDATE_BOOK'))")
    @PutMapping("/{id}")
    CharacterResponseDto updateGenre(@PathVariable(name = "id") Long id,
                                 @RequestBody CharacterRequestDto dto) {
        return characterService.updateCharacter(id, dto);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN') or (hasRole('ADMIN')" +
            " and hasAuthority('CAN_DELETE_BOOK'))")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        characterService.deleteCharacter(id);
        return ResponseEntity.noContent().build();
    }
}
