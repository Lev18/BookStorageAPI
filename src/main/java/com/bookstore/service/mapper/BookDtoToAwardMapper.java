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
    public Map<String, Award> bookToAwardMapper(BookCsvDto bookCsvDto,
                                                Map<String, Award> awardExist,
                                                List<Award> newAwards) {

        Map<String, Award> allAwards = new HashMap<>();
        for (String original : bookCsvDto.getAwards()) {
            String normalized = original.split(" for ")[0].split("\\(")[0].trim().toLowerCase();
            if (!normalized.isEmpty() && !Character.isLetterOrDigit(normalized.charAt(0))) {
                normalized = normalized.substring(1).trim();
            }
            if (normalized.isBlank()) continue;

            Award newAward = awardExist.get(normalized);
            if (newAward == null) {
                newAward = new Award(normalized);
                newAwards.add(newAward);
                awardExist.put(normalized, newAward);
            }
            allAwards.put(original, newAward);
        }

        return allAwards;
    }
}
