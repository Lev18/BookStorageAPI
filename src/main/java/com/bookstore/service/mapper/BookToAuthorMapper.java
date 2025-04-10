package com.bookstore.service.mapper;

import com.bookstore.entity.Author;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.repository.AwardsRepository;
import com.bookstore.service.dto.BookCsvDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@AllArgsConstructor
public class BookToAuthorMapper {
    private final AuthorRepository authorRepository;
    public List<Author> bookToAuthorMapper(BookCsvDto bookDto, Set<Author> authorsInRepository) {
        List<Author> authors = Arrays.stream(bookDto.getAuthor()
                        .split(", "))
                .map(Author::new)
                .toList();

        List<Author> allAuthors = new ArrayList<>();
        List<Author> newAuthors = new ArrayList<>();

        for (Author author : authors) {
            Author exisingAuthor = authorsInRepository.stream()
                    .filter(author1 -> author1.equals(author))
                    .findFirst()
                    .orElse(null);
            if (exisingAuthor != null) {
                allAuthors.add(exisingAuthor);
            } else {
                allAuthors.add(author);
                newAuthors.add(author);
            }
        }

        if (!newAuthors.isEmpty()) {
            authorsInRepository.addAll(newAuthors);
            authorRepository.saveAll(newAuthors);
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

