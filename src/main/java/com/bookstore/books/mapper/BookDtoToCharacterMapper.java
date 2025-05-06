package com.bookstore.books.mapper;

import com.bookstore.books.entity.Characters;
import com.bookstore.books.repository.CharactersRepository;
import com.bookstore.books.dto.csvDto.BookCsvDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class BookDtoToCharacterMapper {
    private final CharactersRepository charactersRepository;
    @Transactional
    public List<Characters> mapBookDtoToCharacter(List<String> bookDto,
                                                  Map<String, Characters> charactersMap,
                                                  List<Characters> newCharacters) {
        List<Characters> allCharacters = new ArrayList<>();

        for (String s : bookDto) {
            if (!s.isBlank()) {
                String cleanCharacter = s.replaceAll("'", "").trim().toLowerCase();

                Characters character = charactersMap.computeIfAbsent(cleanCharacter, chart-> {
                    Characters newChar = new Characters(cleanCharacter);
                    newCharacters.add(newChar);
                    return newChar;
                });

                allCharacters.add(character);
            }
        }

        return allCharacters;
    }
}
