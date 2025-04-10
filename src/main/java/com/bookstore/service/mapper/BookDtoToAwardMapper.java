package com.bookstore.service.mapper;

import com.bookstore.entity.Award;
import com.bookstore.repository.AwardsRepository;
import com.bookstore.service.dto.BookCsvDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Transactional
public class BookDtoToAwardMapper {
    private final AwardsRepository awardsRepository;
    public Map<String, Award> bookToAwardMapper(BookCsvDto bookCsvDto, Set<Award> awardsInDb) {
        List<Award> newAwards = new ArrayList<>();
        Map<String, Award> allAwards = new HashMap<>();
        for (String original :bookCsvDto.getAwards()) {
            String normalized = original.split(" for ")[0].split("\\(")[0];
            if (!normalized.isEmpty() && !Character.isLetterOrDigit(normalized.charAt(0))){
                normalized = normalized.substring(1).trim();
            }

            final String awFinal = normalized;
            Award existAward = awardsInDb.stream()
                    .filter(award ->award.getName().equals(awFinal))
                    .findFirst()
                    .orElseGet(()-> {
                        Award newAward = new Award(awFinal);
                        newAwards.add(newAward);
                        return newAward;
                    });
                allAwards.put(original, existAward);
        }

        awardsRepository.saveAll(newAwards);
        newAwards.clear();

        return allAwards;
    }
}
