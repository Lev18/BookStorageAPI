package com.bookstore.mapper;

import com.bookstore.entity.Format;
import com.bookstore.dto.csvDto.BookCsvDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class BookDtoToFormatMapper {
    public List<Format> mapBookToFormat(BookCsvDto bookDto,
                                        Map<String, Format> allFormatExists,
                                        List<Format> allNewFormats) {
        List<Format> formats = Arrays.stream(bookDto.getBookFormat().split(", "))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Format::new)
                .toList();

        List<Format> allFormats = new ArrayList<>();

        for (Format format : formats) {
            Format existFormat = allFormatExists.get(format.getFormat().toLowerCase());
            if (existFormat != null) {
                allFormats.add(existFormat);
            } else {
                allFormats.add(format);
                    allNewFormats.add(format);
                    allFormatExists.put(format.getFormat().toLowerCase(), format);
            }
        }

        return allFormats;

    }
}
