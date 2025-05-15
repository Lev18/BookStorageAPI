package com.bookstore.books.controller;

import com.bookstore.books.criteria.SearchCriteria;
import com.bookstore.books.dto.responseDto.AwardBookResponseDTO;
import com.bookstore.books.dto.responseDto.AwardResponseDTO;
import com.bookstore.books.service.AwardService;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/awards")
@RequiredArgsConstructor
public class AwardController {
    private final AwardService awardService;
    @GetMapping("")
    public ResponseEntity<Page<AwardResponseDTO>> getAllAwards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setPage(page);
        searchCriteria.setSize(size);
        Pageable pageable = PageRequest.of(page, size);

        return awardService.getAllAwards(searchCriteria,
                        pageable)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Page<AwardBookResponseDTO>> getAwardBooks(
            @PathVariable(name = "id") Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setPage(page);
        searchCriteria.setSize(size);
        Pageable pageable = PageRequest.of(page, size);

        return awardService.getAwardBooks(id, searchCriteria,
                        pageable)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('SUPER_ADMIN') or (hasRole('ADMIN') and hasAuthority('CAN_INSERT_AWARD'))")
    @PostMapping("/{award}")
    public ResponseEntity<String> addNewAward(@PathVariable(name = "award") String award) {
        return ResponseEntity.ok(awardService.addNewAward(award));
    }
}
