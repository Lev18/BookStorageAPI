package com.bookstore.service.mapper;

import com.bookstore.entity.Book;
import com.bookstore.entity.Characters;
import com.bookstore.service.dto.BookCsvDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookDtoToCharacterMapper {
    public List<Characters> mapBookDtoToCharacter(BookCsvDto bookDto, Book book) {
        return bookDto.getCharacters()
                .stream()
                .filter(s -> !s.isEmpty())
                .map(character -> Characters.builder()
                        .charName(character)
                        .book(book)
                        .build())
                .collect(Collectors.toList());
    }
}
