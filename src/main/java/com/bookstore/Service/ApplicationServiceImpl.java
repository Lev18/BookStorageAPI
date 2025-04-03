package com.bookstore.Service;

import com.bookstore.Model.Awards;
import com.bookstore.Model.Book;
import com.bookstore.Model.BookCharacters;
import com.bookstore.Repository.AwardsRepository;
import com.bookstore.Repository.BookRepository;
import com.bookstore.Repository.CharactersRepository;
import com.bookstore.Service.dto.BookCsvDto;
import com.bookstore.Service.mapper.BookDtoToBookDBMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component("appServiceImpl")
public class ApplicationServiceImpl implements ApplicationService{
    @Autowired
    BookDtoToBookDBMapper bookDtoToBookDBMapper;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    private AwardsRepository awardsRepository;
    @Autowired
    private  CharactersRepository charactersRepository;

    @Transactional
    public int saveBook(List<BookCsvDto> bookCsvDtos) {
        List<Book> allBooks = new ArrayList<>();
        Set<Awards> allAwards = new HashSet<>();
        Set<BookCharacters> allCharacters = new HashSet<>();
        for (BookCsvDto bookCsvDto : bookCsvDtos) {
//            ImageLoader.uploadImage(bookCsvDto.getCoverImg());
            Book book = bookDtoToBookDBMapper.bookToAwardMapper(bookCsvDto);
            allBooks.add(book);
            allAwards.addAll(book.getAwards());
            allCharacters.addAll(book.getCharacters());
        }
        bookRepository.saveAll(allBooks);
        awardsRepository.saveAll(allAwards);
        charactersRepository.saveAll(allCharacters);

        return bookCsvDtos.size();
    }

}
