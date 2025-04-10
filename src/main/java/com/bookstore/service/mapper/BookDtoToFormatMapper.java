package com.bookstore.service.mapper;

import com.bookstore.entity.Format;
import com.bookstore.repository.FormatRepository;
import com.bookstore.service.dto.BookCsvDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class BookDtoToFormatMapper {
    private final FormatRepository formatRepository;

    public List<Format> mapBookToFormat(BookCsvDto bookDto, Set<Format> formatsInDb) {
        List<Format> formats = Arrays.stream(bookDto.getBookFormat()
                        .split(", "))
                .map(Format::new)
                .toList();

        List<Format> allFormats = new ArrayList<>();
        List<Format> newFormats = new ArrayList<>();

        for (Format format : formats) {
            Format existFormat = formatsInDb.stream()
                    .filter(format1 -> format1.equals(format))
                    .findFirst()
                    .orElse(null);
            if (existFormat != null) {
                allFormats.add(existFormat);
            } else {
                allFormats.add(format);
                newFormats.add(format);
            }
        }

        if (!newFormats.isEmpty()) {
            formatsInDb.addAll(newFormats);
            formatRepository.saveAll(newFormats);
        }

        return allFormats;

    }
}
