package com.bookstore.Service.mapper;

import com.bookstore.Model.Book;
import com.bookstore.Model.Format;
import com.bookstore.Repository.*;
import com.bookstore.Service.dto.BookCsvDto;
import jakarta.persistence.Column;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class BookDtoToBookDBMapper {
    @Autowired
    BookDtoToAwardMapper awardMapper;
    @Autowired
    BookDtoToCharacterMapper characterMapper;
    @Autowired
    BookDtoToFormatMapper bookDtoFormatMapper;
    @Autowired
    FormatRepository formatRepository;
    @Autowired
    BookToAuthorMapper bookToAuthorMapper;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    BookToPublisherMapper bookDtoToPublisherMapper;
    @Autowired
    PublisherRepository publisherRepository;


    public Book bookToAwardMapper(BookCsvDto bookDto) {
            Book book = new Book();
            book.setBookId(bookDto.getBookId());
            book.setTitle(bookDto.getTitle());
            book.setSeries(bookDto.getSeries());
            book.setRating(bookDto.getRating());
            book.setDescription(bookDto.getDescription());
            book.setLanguage(bookDto.getLanguage());
            book.setIsbn(bookDto.getIsbn());
            book.setEdition(bookDto.getEdition());
            book.setPages(bookDto.getPages());
            book.setPublishDate(bookDto.getPublishDate());
            book.setFirstPublishDate(bookDto.getFirstPublishDate());
            book.setNumRatings(bookDto.getNumRatings());
            book.setLikedPercent(bookDto.getLikedPercent());
            book.setCoverImg(bookDto.getCoverImg());
            book.setBbeScore(bookDto.getBbeScore());
            book.setBbeVotes(bookDto.getBbeVotes());
            book.setPrice(bookDto.getPrice());
            book.setAwards(awardMapper.bookToAwardMapper(bookDto, book));
            book.setCharacters(characterMapper.mapBookDtoToCharacter(bookDto, book));
            book.setFormat(bookDtoFormatMapper.mapBookToFormat(bookDto, formatRepository));
            book.setAuthor(bookToAuthorMapper.bookToAuthorMapper(bookDto, authorRepository));
            book.setPublisher(bookDtoToPublisherMapper.bookToPublisherMapper(bookDto, publisherRepository));

        return book;
    }
}
