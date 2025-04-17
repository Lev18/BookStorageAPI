package com.bookstore.service.mapper;

import com.bookstore.entity.Book;
import com.bookstore.dto.csvDto.BookCsvDto;
import com.bookstore.enums.Language;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class BookDtoToBookDBMapper {
    private final BookDtoToRatingsMapper ratingMapper;

    public Book bookDtoToBookMapper(BookCsvDto bookDto) {
            Book book = new Book();
            book.setBookId(bookDto.getBookId());
            book.setTitle(bookDto.getTitle());
            book.setRating(bookDto.getRating());
            book.setDescription(bookDto.getDescription());
            book.setLanguage(Language.fromLanguageName(bookDto.getLanguage()));
            book.setIsbn(bookDto.getIsbn());
            book.setEdition(bookDto.getEdition());
            book.setPages(bookDto.getPages());
            book.setPublishDate(bookDto.getPublishDate());
            book.setFirstPublishDate(bookDto.getFirstPublishDate());
            book.setLikedPercent(bookDto.getLikedPercent());
            book.setCoverImg(bookDto.getCoverImg());
            book.setBbeScore(bookDto.getBbeScore());
            book.setBbeVotes(bookDto.getBbeVotes());
            book.setPrice(bookDto.getPrice());

            //  relational databases configurations
            book.setStars(ratingMapper.bookToRatingsMapper(bookDto, book));
            book.setNumRatingByStars(book.getStars());

            return book;
    }
}
