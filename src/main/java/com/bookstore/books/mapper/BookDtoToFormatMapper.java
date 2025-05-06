package com.bookstore.books.mapper;

import com.bookstore.books.entity.Format;
import com.bookstore.books.dto.csvDto.BookCsvDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

@Component
@AllArgsConstructor
public class BookDtoToFormatMapper {
    public <T> List<Format> mapBookToFormat(T bookDto,
                                            Function<T, Collection<String>> formatExtractor,
                                            Map<String, Format> allFormatExists,
                                            List<Format> allNewFormats) {

        Collection<String> formats = formatExtractor.apply(bookDto);
        return formatMapper(formats, allFormatExists, allNewFormats);
    }
    private List<Format> formatMapper(Collection<String> formats,
                                      Map<String, Format> allFormatExists,
                                      List<Format> allNewFormats) {
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
