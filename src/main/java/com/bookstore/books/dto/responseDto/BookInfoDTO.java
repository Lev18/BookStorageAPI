package com.bookstore.books.dto.responseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class BookInfoDTO {
    private Long id;
    private String title;
    private Set<String> author = new LinkedHashSet<>();
    private Double rating;
    private String edition;
    private String description;
    private Set<String> genres = new LinkedHashSet<>();
    private String pages;
    private String publishDate;
    private String firstPublishDate;
    private String price;
    private Set<String> awards = new LinkedHashSet<>();
    private Set<String> settings = new LinkedHashSet<>();
    private Set<String> characters = new LinkedHashSet<>();
    private Set<String> publishers = new LinkedHashSet<>();
    private String language;
    private String ISBN;
    private String series;
    private String formats;
    private String coverImgURL;

    public BookInfoDTO(Long id,
                       String title,
                       Set<String> author,
                       String isbn,
                       String description,
                       String pages,
                       String publishDate,
                       String language,
                       Set<String> awards,
                       Set<String> characters,
                       Set<String> genres,
                       Set<String> settings,
                       Double rating) {
        this.id = id;
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

    public BookInfoDTO(Long id,
                       String title,
                       String isbn,
                       String description,
                       String pages,
                       String publishDate,
                       String language,

                       Double rating) {
        this.id = id;
        this.title = title;
        this.ISBN = isbn;
        this.description = description;
        this.pages = pages;
        this.publishDate = publishDate;
        this.language = language;
        this.rating = rating;
    }


}
