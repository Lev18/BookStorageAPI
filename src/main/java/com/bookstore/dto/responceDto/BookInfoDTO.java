package com.bookstore.dto.responceDto;

import com.bookstore.enums.Language;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BookInfoDTO {
    private String title;
    private List<String> author;
    private Double rating;
    private String edition;
    private String description;
    private List<String> genres;
    private String pages;
    private String publishDate;
    private String firstPublishDate;
    private String price;
    private List<String> awards;
    private List<String> settings;
    private List<String> characters;
    private String language;
    private String ISBN;
    private String series;
    private String formats;
    private String coverImgURL;

    public BookInfoDTO(String title,
                       List<String> author,
                       String isbn,
                       String description,
                       String pages,
                       String publishDate,
                       String language,
                       List<String>awards,
                       List<String> characters,
                       List<String> genres,
                       List<String> settings,
                       Double rating) {
        this.title = title;
        this.author = author;
        this.ISBN = isbn;
        this.description = description;
        this.pages = pages;
        this.publishDate = publishDate;
        this.language = language;
        this.awards = awards;
        this.characters = characters;
        this.genres = genres;
        this.rating = rating;
        this.settings = settings;
    }
}
