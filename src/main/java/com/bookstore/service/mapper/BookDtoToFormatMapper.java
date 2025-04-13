package com.bookstore.service.mapper;

import com.bookstore.entity.Format;
import com.bookstore.repository.FormatRepository;
import com.bookstore.service.dto.BookCsvDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class BookDtoToFormatMapper {
//    private final FormatRepository formatRepository;

    public List<Format> mapBookToFormat(BookCsvDto bookDto,
                                        Map<String, Format> allFormatExists,
                                        List<Format> allNewFormats) {
        List<Format> formats = Arrays.stream(bookDto.getBookFormat()
                        .split(", "))
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
