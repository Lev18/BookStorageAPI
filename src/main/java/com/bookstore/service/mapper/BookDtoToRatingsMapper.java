package com.bookstore.service.mapper;

import com.bookstore.entity.Book;
import com.bookstore.entity.RatingByStars;
import com.bookstore.service.dto.BookCsvDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookDtoToRatingsMapper {
    public List<RatingByStars> bookToRatingsMapper(BookCsvDto bookCsvDto, Book book) {
        List<RatingByStars> ratings = new ArrayList<>();
        short gradesByFive = 5;
           for (String rate : bookCsvDto.getRatingsByStars()) {
               RatingByStars rating = new RatingByStars();
               if (!rate.isEmpty() && !rate.isBlank()) {
                   rating.setRatingStars(rate);
                   rating.setBook(book);
                   rating.setGrade(gradesByFive--);
                   ratings.add(rating);
               }
        }
        return ratings;
    }
}
