package com.bookstore.books.mapper;

import com.bookstore.books.entity.Format;
import com.bookstore.books.dto.csvDto.BookCsvDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class BookDtoToFormatMapper {
    public List<Format> mapBookToFormat(BookCsvDto bookDto,
                                        Map<String, Format> allFormatExists,
                                        List<Format> allNewFormats) {
        List<String> formats = Arrays.stream(bookDto.getBookFormat().split(", "))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        List<Format> allFormats = new ArrayList<>();

        for (String format : formats) {
            String cleanString = format.trim().toLowerCase();
            Format existFormat = allFormatExists.computeIfAbsent(cleanString, key->{
               Format newFormat = new Format(cleanString);
               allNewFormats.add(newFormat);
               return  newFormat;
            });
            allFormats.add(existFormat);
        }
        return allFormats;
    }
}
