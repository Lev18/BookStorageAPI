package com.bookstore.books.service;

import com.bookstore.books.criteria.SearchCriteria;
import com.bookstore.books.dto.responseDto.AwardBookResponseDTO;
import com.bookstore.books.dto.responseDto.AwardResponseDTO;
import com.bookstore.books.entity.Award;
import com.bookstore.books.repository.AwardsRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotEmpty;
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

    public Optional<Page<AwardResponseDTO>> getAllAwards(SearchCriteria searchCriteria,
                                                         Pageable pageable) {
        return awardsRepository.findAllWithBooks(searchCriteria, pageable)
                .map(this::toAwDtoPage);
    }

    private Page<AwardResponseDTO> toAwDtoPage(@NotNull Page<Object[]> objects) {
        return objects.map(this::toAwardDto);
    }

    private AwardResponseDTO toAwardDto(@NotNull Object[] award) {

       AwardResponseDTO awardResponseDTO = new AwardResponseDTO();
       awardResponseDTO.setAwardId(((Award)award[0]).getId());
       awardResponseDTO.setAwardName(((Award)award[0]).getName());
       awardResponseDTO.setAllBooks((Long)award[1]);

       return awardResponseDTO;
    }

    public Optional<Page<AwardBookResponseDTO>> getAwardBooks(Long id,
                                                              SearchCriteria searchCriteria,
                                                              Pageable pageable) {
        return awardsRepository.findWithBooks(id,
                        searchCriteria,
                        pageable)
                .map(this::toDtoPage);
    }

    private Page<AwardBookResponseDTO> toDtoPage(@NotNull Page<Object[]> objects) {
        return  objects.map(this::toAwardBookDTO);
    }

    private AwardBookResponseDTO toAwardBookDTO(Object[] objects) {
        return new AwardBookResponseDTO(
                (Long) objects[0],
                (Long) objects[1],
                (String) objects[2],
                (String) objects[3],
                (String) objects[4],
                (String) objects[5],
                Arrays.stream(objects[6]
                        .toString().split(","))
                        .toList()
        );
    }

    public AwardResponseDTO addNewAward(@NotEmpty String awardTitle) {
        Award award = new Award();
        award.setName(awardTitle);
        awardsRepository.save(award);

        return this.mapToAward(award,0l);
    }

    private AwardResponseDTO mapToAward(Award award, Long bookCount) {
        return new AwardResponseDTO(award.getId(),
                award.getName(), bookCount);
    }


    public void deleteAward(Long id) {
      if (!awardsRepository.existsById(id)) {
         throw  new EntityNotFoundException("Award not found " + id);
      }
        awardsRepository.deleteById(id);
    }

    public AwardResponseDTO updateAward(Long id,
                                        AwardResponseDTO awardResponseDTO) {
        Award award = awardsRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Award not found"));

        award.setName(awardResponseDTO.getAwardName());

        Award saved = awardsRepository.save(award);
        return this.mapToAward(saved, awardResponseDTO.getAllBooks());
    }
}
