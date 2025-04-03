package com.bookstore.Service;

import com.bookstore.Model.Awards;
import com.bookstore.Model.Book;
import com.bookstore.Repository.AwardsRepository;
import com.bookstore.Repository.BookRepository;
import com.bookstore.Service.FileReader.CsvReaderService;
import com.bookstore.Service.dto.BookCsvDto;
import com.bookstore.Service.mapper.BookDtoToAwardMapper;
import com.bookstore.Service.mapper.BookDtoToBookDBMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("appServiceImpl")
public class ApplicationServiceImpl implements ApplicationService{
    @Autowired
    BookDtoToBookDBMapper bookDtoToBookDBMapper;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    private AwardsRepository awardsRepository;

    public void saveBook(List<BookCsvDto> bookCsvDtos) {
        for (BookCsvDto bookCsvDto : bookCsvDtos) {
            Book book = bookDtoToBookDBMapper.bookToAwardMapper(bookCsvDto);
            bookRepository.save(book);
            awardsRepository.saveAll(book.getAwards());
        }
       // List<Awards> awards = awardMapper.bookToAwardMapper(bookCsvDtos);



//        for (Awards aw :awards) {
//           awardsRepository.save(aw);
//        }

       // awardsRepository.saveAll(awards);
    }

}
