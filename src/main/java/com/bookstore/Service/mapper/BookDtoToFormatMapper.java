package com.bookstore.Service.mapper;

import com.bookstore.Model.Format;
import com.bookstore.Repository.FormatRepository;
import com.bookstore.Service.dto.BookCsvDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BookDtoToFormatMapper {
    private Map<String, Format> formatCache = new HashMap<>();
    public Format mapBookToFormat(BookCsvDto bookDto, FormatRepository formatRepositor ) {
        String bookFormat = bookDto.getBookFormat();
        if (formatCache.containsKey(bookFormat)) {
            return formatCache.get(bookFormat);
        }

        Format format = formatRepositor.findByBookFormat(bookDto.getBookFormat())
                .orElseGet(()->formatRepositor.save(new Format(bookDto.getBookFormat())));
        formatCache.put(bookFormat, format);
        return format;
    }
}
