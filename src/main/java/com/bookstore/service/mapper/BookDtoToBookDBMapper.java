package com.bookstore.service.mapper;

import com.bookstore.entity.Book;
import com.bookstore.repository.*;
import com.bookstore.service.dto.BookCsvDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class BookDtoToBookDBMapper {
    @Autowired
    BookDtoToAwardMapper awardMapper;
    @Autowired
    BookDtoToRatingsMapper ratingMapper;
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

            //  relational databases configurations
            book.setAwards(awardMapper.bookToAwardMapper(bookDto, book));
            book.setStars(ratingMapper.bookToRatingsMapper(bookDto, book));
            book.setCharacters(characterMapper.mapBookDtoToCharacter(bookDto, book));
            book.setFormat(bookDtoFormatMapper.mapBookToFormat(bookDto, formatRepository));
            book.setPublisher(bookDtoToPublisherMapper.bookToPublisherMapper(bookDto, publisherRepository));

        return book;
    }
}
