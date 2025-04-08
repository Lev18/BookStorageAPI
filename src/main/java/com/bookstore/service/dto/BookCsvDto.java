package com.bookstore.Service.dto;


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
    public String bookId;

    @CsvBindByName(column = "title")
    public String title;

    @CsvBindByName(column = "series")
    public String series;
    @CsvBindByName(column = "author")
    public String author;
    @CsvBindByName(column = "rating")
    public String rating;
    @CsvBindByName(column = "description")
    public String description;
    @CsvBindByName(column = "language")
    public String language;
    @CsvBindByName(column = "isbn")
    public String isbn;
    @CsvCustomBindByName(column = "genres", converter = StringToListConverter.class)
    public List<String> genres;
    @CsvCustomBindByName(column = "characters", converter = StringToListConverter.class)
    public List<String> characters;
    @CsvBindByName(column = "bookFormat")
    public String bookFormat;
    @CsvBindByName(column = "edition")
    public String edition;
    @CsvBindByName(column = "pages")
    public String pages;
    @CsvBindByName(column = "publisher")
    public String publisher;
    @CsvBindByName(column = "publishDate")
    public String publishDate;
    @CsvBindByName(column = "firstPublishDate")
    public String firstPublishDate;
    @CsvCustomBindByName(column = "awards", converter = StringToListConverter.class)
    public List<String> awards;
    @CsvBindByName(column = "numRatings")
    public Integer numRatings;
    @CsvCustomBindByName(column = "ratingsByStars", converter = StringToListConverter.class)
    public List<String> ratingsByStars;
    @CsvBindByName(column = "likedPercent")
    public String likedPercent;
    @CsvCustomBindByName(column = "setting",converter = StringToListConverter.class)
    public List<String> setting;
    @CsvBindByName(column = "coverImg")
    public String coverImg;
    @CsvBindByName(column = "bbeScore")
    public String bbeScore;
    @CsvBindByName(column = "bbeVotes")
    public String bbeVotes;
    @CsvBindByName(column = "price")
    public String price;

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
