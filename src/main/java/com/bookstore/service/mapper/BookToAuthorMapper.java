package com.bookstore.service.mapper;

import com.bookstore.entity.Author;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.dto.csvDto.BookCsvDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class BookToAuthorMapper {
    private final AuthorRepository authorRepository;
    public List<Author> bookToAuthorMapper(BookCsvDto bookDto,
                                           Map<String, Author> authorsInRepository,
                                           List<Author> newAuthors) {
        List<Author> authors = Arrays.stream(bookDto.getAuthor()
                        .split(", "))
                .map(Author::new)
                .toList();

        List<Author> allAuthors = new ArrayList<>();

        for (Author author : authors) {
            Author exisingAuthor = authorsInRepository.get(author.getAuthorName().trim().toLowerCase());
            if (exisingAuthor != null) {
                allAuthors.add(exisingAuthor);
            } else {
                allAuthors.add(author);
                newAuthors.add(author);
                authorsInRepository.put(author.getAuthorName().trim().toLowerCase(), author);
            }
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

