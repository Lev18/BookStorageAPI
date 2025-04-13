package com.bookstore.service.mapper;

import com.bookstore.entity.Book;
import com.bookstore.entity.BookCharacter;
import com.bookstore.entity.Characters;
import com.bookstore.repository.CharactersRepository;
import com.bookstore.service.dto.BookCsvDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
                String cleanCharacter = s.replaceAll("'", "").trim();
                String uniqueCharacter = !bookDto.getSeries().isBlank() ?
                        cleanCharacter + "|" + bookDto.getSeries().split("#")[0].trim():
                        cleanCharacter + "|" +bookDto.getTitle().trim();

                Characters character = charactersMap.get(uniqueCharacter.toLowerCase());
                if (character == null) {
                    character = new Characters(cleanCharacter);
                    newCharacters.add(character);
                    charactersMap.put(uniqueCharacter.toLowerCase(), character);
                }
                allCharacters.add(character);
            }
        }

        return allCharacters;
    }
}
