package com.bookstore.dto.csvDto;


import com.bookstore.utils.StringToListConverter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class BookCsvDto {

    @CsvBindByName(column = "bookId")
    private String bookId;

    @CsvBindByName(column = "title")
    private String title;

    @CsvBindByName(column = "series")
    private String series;

    @CsvBindByName(column = "author")
    private String author;

    @CsvBindByName(column = "rating")
    private String rating;

    @CsvBindByName(column = "description")
    private String description;

    @CsvBindByName(column = "language")
    private String language;

    @CsvBindByName(column = "isbn")
    private String isbn;

    @CsvCustomBindByName(column = "genres", converter = StringToListConverter.class)
    private List<String> genres;

    @CsvCustomBindByName(column = "characters", converter = StringToListConverter.class)
    private List<String> characters;

    @CsvBindByName(column = "bookFormat")
    private String bookFormat;

    @CsvBindByName(column = "edition")
    private String edition;

    @CsvBindByName(column = "pages")
    private String pages;

    @CsvBindByName(column = "publisher")
    private String publisher;

    @CsvBindByName(column = "publishDate")
    private String publishDate;

    @CsvBindByName(column = "firstPublishDate")
    private String firstPublishDate;

    @CsvCustomBindByName(column = "awards", converter = StringToListConverter.class)
    private List<String> awards;

    @CsvBindByName(column = "numRatings")
    private Integer numRatings;

    @CsvCustomBindByName(column = "ratingsByStars", converter = StringToListConverter.class)
    private List<String> ratingsByStars;

    @CsvBindByName(column = "likedPercent")
    private String likedPercent;

    @CsvCustomBindByName(column = "setting",converter = StringToListConverter.class)
    private List<String> setting;

    @CsvBindByName(column = "coverImg")
    private String coverImg;

    @CsvBindByName(column = "bbeScore")
    private String bbeScore;

    @CsvBindByName(column = "bbeVotes")
    private String bbeVotes;

    @CsvBindByName(column = "price")
    private String price;

    @Override
    public String toString() {
        return "BookCsvDto{" +
                "bookId='" + bookId + '\'' +
                ", title='" + title + '\'' +
                ", series='" + series + '\'' +
                ", author='" + author + '\'' +
                ", rating='" + rating + '\'' +
                ", description='" + description + '\'' +
                ", language='" + language + '\'' +
                ", isbn='" + isbn + '\'' +
                ", genres=" + genres +
                '}';
    }
}
