package com.bookstore.books.mapper;

import com.bookstore.books.dto.responseDto.BookInfoDTO;
import com.bookstore.books.entity.Author;
import com.bookstore.books.repository.AuthorRepository;
import com.bookstore.books.dto.csvDto.BookCsvDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class BookToAuthorMapper {
    private final AuthorRepository authorRepository;
    public List<Author> bookToAuthorMapper(BookCsvDto bookDto,
                                           Map<String, Author> authorsExists,
                                           List<Author> newAuthors) {
        List<String> authors = Arrays.stream(bookDto.getAuthor()
                        .split(", "))
                .toList();

        List<Author> allAuthors = new ArrayList<>();

        for (String author : authors) {
            Author exisingAuthor = authorsExists.computeIfAbsent(author.trim().toLowerCase(),
                    auth-> {
                        Author newAuth = new Author(author);
                        newAuthors.add(newAuth);
                        return newAuth;
                    });
            allAuthors.add(exisingAuthor);
        }

        return allAuthors;
    }

    public List<Author> mapBookDtoToAuthor(BookInfoDTO bookInfoDTO) {
        return null;
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

