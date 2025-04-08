package com.bookstore.Service.mapper;

import com.bookstore.Model.Awards;
import com.bookstore.Model.Book;
import com.bookstore.Repository.AwardsRepository;
import com.bookstore.Service.dto.BookCsvDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
