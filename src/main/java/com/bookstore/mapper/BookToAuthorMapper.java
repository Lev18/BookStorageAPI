package com.bookstore.mapper;

import com.bookstore.dto.responseDto.BookInfoDTO;
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
                                           Map<String, Author> authorsExists,
                                           List<Author> newAuthors) {
        List<Author> authors = Arrays.stream(bookDto.getAuthor()
                        .split(", "))
                .map(Author::new)
                .toList();

        List<Author> allAuthors = new ArrayList<>();

        for (Author author : authors) {
            Author exisingAuthor = authorsExists.get(author.getName().trim().toLowerCase());
            if (exisingAuthor != null) {
                allAuthors.add(exisingAuthor);
            } else {
                allAuthors.add(author);
                    newAuthors.add(author);
                    authorsExists.put(author.getName().trim().toLowerCase(), author);
            }
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

