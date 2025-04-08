package com.bookstore.service.mapper;

import com.bookstore.entity.Awards;
import com.bookstore.entity.Book;
import com.bookstore.service.dto.BookCsvDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookDtoToAwardMapper {
    public List<Awards> bookToAwardMapper(BookCsvDto bookCsvDto, Book book) {
        List<Awards> awards = new ArrayList<>();
           for (String award : bookCsvDto.getAwards()) {
               Awards awards1 = new Awards();
               if (!award.isBlank()) {
                   awards1.setAwardTitle(award);
                   awards1.setBook(book);
                   awards.add(awards1);
               }
        }
        return awards;
    }
}
