package com.bookstore.books.mapper;

import com.bookstore.books.dto.responseDto.BookInfoDTO;
import com.bookstore.books.entity.Book;
import com.bookstore.books.enums.Language;
import com.bookstore.books.repository.BookRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
@RequiredArgsConstructor
public class BookInfoDtoToBookMapper {
    private final BookRepository bookRepository;

    public Book mapBootDtoIntoBook(BookInfoDTO bookDto) {
        if(bookRepository.findBookByIsbn(bookDto.getISBN().trim()) != null) {
            return null;
        }

        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setDescription(bookDto.getDescription());
        book.setLanguage(Language.fromLanguageName(bookDto.getLanguage()));
        book.setIsbn(bookDto.getISBN());
        book.setEdition(bookDto.getEdition());
        book.setPages(bookDto.getPages());
        book.setPublishDate(bookDto.getPublishDate());
        book.setFirstPublishDate(bookDto.getFirstPublishDate());
        book.setCoverImg(bookDto.getCoverImgURL());
        book.setPrice(bookDto.getPrice());

        return book;
    }
}
