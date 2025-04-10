package com.bookstore.service.mapper;

import com.bookstore.entity.Book;
import com.bookstore.entity.BookCharacter;
import com.bookstore.entity.Characters;
import com.bookstore.repository.CharactersRepository;
import com.bookstore.service.dto.BookCsvDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookDtoToCharacterMapper {
    private final CharactersRepository charactersRepository;
    @Transactional
    public List<Characters> mapBookDtoToCharacter(BookCsvDto bookDto,
                                                  Set<Characters> charactersInDb) {
        List<Characters> newCharacters = new ArrayList<>();
        List<Characters> allCharacters = new ArrayList<>();

        for (String s : bookDto.getCharacters()) {
            if (!s.isBlank()) {
                String cleanCharacter = s.replaceAll("'", "").trim();
                Characters character = charactersInDb.stream()
                        .filter(characters -> characters.getCharacterName().equalsIgnoreCase(cleanCharacter))
                        .findFirst()
                        .orElseGet(() -> {
                            Characters chart = new Characters(cleanCharacter);
                            newCharacters.add(chart);
                            return chart;
                        });
                allCharacters.add(character);
            }
        }
        charactersRepository.saveAll(newCharacters);
        charactersInDb.addAll(newCharacters);
        return allCharacters;
    }
}
