package com.bookstore.Service.mapper;

import com.bookstore.Model.Author;
import com.bookstore.Repository.AuthorRepository;
import com.bookstore.Service.dto.BookCsvDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BookToAuthorMapper {
    // check in db no in memory
    private Map<String, Author> authorCache = new HashMap<>();

    public Author bookToAuthorMapper(BookCsvDto bookDto, AuthorRepository authorRepository) {
        String bookFormat = bookDto.getBookFormat();
        if (authorCache.containsKey(bookFormat)) {
            return authorCache.get(bookFormat);
        }

        Author author = authorRepository.findByAuthorName(bookDto.getAuthor())
                .orElseGet(()->authorRepository.save(new Author(bookDto.getAuthor())));
        authorCache.put(bookFormat, author);
        return author;
    }
}
