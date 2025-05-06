package com.bookstore.books.mapper;

import com.bookstore.books.entity.Award;
import com.bookstore.books.dto.csvDto.BookCsvDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Transactional
public class BookDtoToAwardMapper {
    public Map<String, Award> bookToAwardMapper(List<String> bookCsvDto,
                                                Map<String, Award> awardExist,
                                                List<Award> newAwards) {

        Map<String, Award> allAwards = new HashMap<>();
        for (String original : bookCsvDto) {
            String normalized = original.split(" for ")[0]
                    .split("\\(")[0]
                    .trim().toLowerCase()
                    .replaceAll("'", "");

            if (!normalized.isEmpty() && !Character.isLetterOrDigit(normalized.charAt(0))) {
                normalized = normalized.substring(1).trim();
            }
            if (normalized.isBlank()) continue;

            Award newAward = awardExist.computeIfAbsent(normalized,award-> {
                    Award newAw = new Award(award);
                    newAwards.add(newAw);
                    return newAw;
                }
            );

            allAwards.put(original.trim().replaceAll("'", ""), newAward);
        }
        return allAwards;
    }

}
