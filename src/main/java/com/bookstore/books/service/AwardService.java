package com.bookstore.books.service;

import com.bookstore.books.criteria.SearchCriteria;
import com.bookstore.books.dto.responseDto.AwardResponseDTO;
import com.bookstore.books.entity.Award;
import com.bookstore.books.repository.AwardsRepository;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.*;

@Service
@Setter
@Getter
@RequiredArgsConstructor
public class AwardService {
    private final AwardsRepository awardsRepository;

    public Optional<Page<AwardResponseDTO>> getAllAwards(SearchCriteria searchCriteria, Pageable pageable) {
        return awardsRepository.findAllWithBooks(searchCriteria, pageable)
                .map(this::toDtoPage);
    }

    private Page<AwardResponseDTO> toDtoPage(@NotNull Page<Object[]> objects) {
        return objects.map(this::toDto);
    }

    private AwardResponseDTO toDto(@NotNull Object[] award) {

       AwardResponseDTO awardResponseDTO = new AwardResponseDTO();
       awardResponseDTO.setAwardId(((Award)award[0]).getId());
       awardResponseDTO.setAwardName(((Award)award[0]).getName());
       awardResponseDTO.setAllBooks((Long)award[1]);

       return awardResponseDTO;
    }
}
