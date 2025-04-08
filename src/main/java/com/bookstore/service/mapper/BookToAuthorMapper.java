package com.bookstore.service.mapper;

import com.bookstore.entity.Author;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.service.dto.BookCsvDto;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BookToAuthorMapper {
    // check in db not in memory
    public List<Author> bookToAuthorMapper(BookCsvDto bookDto, Set<Author> authorsInRepository) {
        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        List<Author> authors = Arrays.stream(bookDto.getAuthor()
                        .split(", "))
                .map(authorName -> new Author(authorName))
                .filter(author -> !authorsInRepository.contains(author))
                .toList();
        authorsInRepository.addAll(authors);
        return authors;
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
    }
}
