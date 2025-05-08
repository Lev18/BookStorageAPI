package com.bookstore.books.dto.responseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class BookResponseDto {
    private Long id;
    private String title;
    private String isbn;
    private String series;
    private String publishDate;
    private Integer rating;

    private Set<String> authors = new LinkedHashSet<>();
    private Set<String> awards = new LinkedHashSet<>();
    private Set<String> settings = new LinkedHashSet<>();
    private Set<String> characters = new LinkedHashSet<>();
    private Set<String> genres = new LinkedHashSet<>();
    private Set<String> publishers = new LinkedHashSet<>();

    public BookResponseDto(Long id,
                           String title,
                           String isbn,
                           String series,
                           String publishDate,
                           Integer rating) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.series = series;
        this.publishDate = publishDate;
        this.rating = rating;
    }

    public void addAuthor(String author) {
        if (author != null) this.authors.addAll(
                Arrays.stream(author.split(",")).toList());
    }

    public void addAward(String award) {

        if (award != null) this.awards.addAll(
                Arrays.stream(award.split(",")).toList());
    }

    public void addGenre(String genre) {
        if (genre != null) this.genres.addAll(
                Arrays.stream(genre.split(",")).toList());
    }

    public void addPublisher(String publisher) {
        if (publisher != null) this.publishers.addAll(
                Arrays.stream(publisher.split(",")).toList());
    }

    public void addCharacter(String author) {
        if (author != null) this.characters.addAll(
                Arrays.stream(author.split(",")).toList());
    }

    public void addSettings(String setting) {
        if (setting != null && !setting.isEmpty()) this.settings.addAll(
                Arrays.stream(setting.split(",")).toList()
        );
    }
}
