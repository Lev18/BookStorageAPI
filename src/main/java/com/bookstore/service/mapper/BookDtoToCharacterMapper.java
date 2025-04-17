package com.bookstore.service.mapper;

import com.bookstore.entity.Characters;
import com.bookstore.repository.CharactersRepository;
import com.bookstore.dto.csvDto.BookCsvDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class BookDtoToCharacterMapper {
    private final CharactersRepository charactersRepository;
    @Transactional
    public List<Characters> mapBookDtoToCharacter(BookCsvDto bookDto,
                                                  Map<String, Characters> charactersMap,
                                                  List<Characters> newCharacters) {
        List<Characters> allCharacters = new ArrayList<>();

        for (String s : bookDto.getCharacters()) {
            if (!s.isBlank()) {
                String cleanCharacter = s.replaceAll("'", "").trim().toLowerCase();

                Characters character = charactersMap.get(cleanCharacter);
                if (character == null) {
                    character = new Characters(cleanCharacter);
                    newCharacters.add(character);
                    charactersMap.put(cleanCharacter, character);
                }
                allCharacters.add(character);
            }
        }

        return allCharacters;
    }
}
