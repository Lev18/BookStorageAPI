package com.bookstore.books.mapper;

import com.bookstore.books.dto.requestDto.BookRequestDto;
import com.bookstore.books.entity.Book;
import com.bookstore.books.dto.csvDto.BookCsvDto;
import com.bookstore.books.enums.Language;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class BookDtoToBookDBMapper {
    private final BookDtoToRatingsMapper ratingMapper;

    public Book bookDtoToBookMapper(BookCsvDto bookCsvDto) {
            return mapCommonFields(bookCsvDto);
    }

    public Book bookDtoToBookMapper(BookRequestDto bookCsvDto) {
            return mapCommonFields(bookCsvDto);
    }

    public Book mapCommonFields(Object bookDto) {
            Book newBook = new Book();
            if (bookDto instanceof BookCsvDto) {
                    mapFields(newBook, (BookCsvDto)bookDto);
            } else if (bookDto instanceof BookRequestDto) {
                    mapFields(newBook, (BookRequestDto) bookDto);
            }
            return newBook;
    }

    public void mapFields (Book book, BookCsvDto bookDto) {
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
            if (!bookDto.getSeries().isBlank() && !bookDto.getSeries().equals("\"\"")) {
                    if (bookDto.getSeries().contains("#")) {
                            book.setSeriesNumber(bookDto.getSeries().split("#")[1].trim());
                    }
                    else {
                         book.setSeriesNumber(bookDto.getSeries());
                    }
            }

            //  relational databases configurations
            book.setStars(ratingMapper.bookToRatingsMapper(bookDto, book));
            book.setNumRatingByStars(book.getStars());

    }

        public void mapFields (Book book, BookRequestDto bookDto) {
                book.setBookId(bookDto.getBookId());
                book.setTitle(bookDto.getTitle());
                book.setDescription(bookDto.getDescription());
                book.setLanguage(Language.fromLanguageName(bookDto.getLanguage()));
                book.setIsbn(bookDto.getIsbn());
                book.setEdition(bookDto.getEdition());
                book.setPages(bookDto.getPages());
                book.setPublishDate(bookDto.getPublishDate());
                book.setFirstPublishDate(bookDto.getFirstPublishDate());
                book.setPrice(bookDto.getPrice());
                if (!bookDto.getSeries().isBlank() && !bookDto.getSeries().equals("\"\"")) {
                        if (bookDto.getSeries().contains("#")) {
                                book.setSeriesNumber(bookDto.getSeries().split("#")[1].trim());
                        }
                        else {
                                book.setSeriesNumber(bookDto.getSeries());
                        }
                }
        }
}
