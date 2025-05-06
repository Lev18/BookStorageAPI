package com.bookstore.books.mapper;

import com.bookstore.books.dto.requestDto.BookRequestDto;
import com.bookstore.books.dto.responseDto.BookInfoDTO;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@NoArgsConstructor
public class BookToInfoDtoMapper {
    public BookInfoDTO mapBookRequestToInfo(Long id, BookRequestDto book) {
        BookInfoDTO bookInfoDTO = new BookInfoDTO(
                id,
                book.getTitle(),
                Set.copyOf(book.getAuthor()),
                book.getIsbn(),
                book.getDescription(),
                book.getPages(),
                book.getPublishDate(),
                book.getLanguage(),
                Set.copyOf(book.getAwards()),
                Set.copyOf(book.getCharacters()),
                Set.copyOf(book.getGenres()),
                Set.copyOf(book.getSettings()),
                0.0);

        return bookInfoDTO;
    }
}
