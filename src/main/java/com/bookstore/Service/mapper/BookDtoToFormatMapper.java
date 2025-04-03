package com.bookstore.Service.mapper;

import com.bookstore.Model.Format;
import com.bookstore.Service.dto.BookCsvDto;
import org.springframework.stereotype.Component;

@Component
public class BookDtoToFormatMapper {
    public Format bookToAwardMapper(BookCsvDto bookDto) {
        Format format = new Format();
        return format;
    }
}
