package com.bookstore.books.mapper;

import com.bookstore.books.dto.requestDto.BookRequestDto;
import com.bookstore.books.dto.responseDto.BookInfoDTO;
import com.bookstore.books.entity.Author;
import com.bookstore.books.repository.AuthorRepository;
import com.bookstore.books.dto.csvDto.BookCsvDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

@Component
@AllArgsConstructor
public class BookToAuthorMapper {

    public <T> List<Author> mapBookToAuthor(
            T bookDto,
            Function<T, Collection<String>> authorExtractor,
            Map<String, Author> authorsExists,
            List<Author> newAuthors
    ) {
        Collection<String> authors = authorExtractor.apply(bookDto);
        return mapAuthors(authors, authorsExists, newAuthors);
    }

    private List<Author> mapAuthors(Collection<String> authors,
                                    Map<String, Author> authorsExists,
                                    List<Author> newAuthors) {
        List<Author> allAuthors = new ArrayList<>();
        for (String author : authors) {
            Author exisingAuthor = authorsExists.computeIfAbsent(author
                            .trim().toLowerCase(),
                    auth-> {
                        Author newAuth = new Author(author);
                        newAuthors.add(newAuth);
                        return newAuth;
                    });
            allAuthors.add(exisingAuthor);
        }
        return allAuthors;
    }
}

//        Pattern pattern = Pattern.compile("\\((.*?)\\)")
//        List<String> roles = new ArrayList<>();
//        Map<String, List<String>> authorsRole = new HashMap<>();
//        for (String s : authors) {
//            Matcher matcher = pattern.matcher(s);
//            String[] author = s.split("\\(");
//            while (matcher.find()) {
//                roles.add(matcher.group(1).trim());
//            }
//            authorsRole.put(author[0].trim(),
//                    new ArrayList<>(roles));
//            roles.removeAll(roles);

