package com.bookstore.books.dto.requestDto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class BookRequestDto {
    @NotNull
    private String bookId;

    @NotNull
    private String title;

    private List<String> author;
    private String edition;

    @NotNull
    private String description;

    @NotNull
    private List<String> genres;

    @NotBlank
    private String pages;

    @NotBlank
    private String publishDate;

    private String firstPublishDate;
    private String price;
    private List<String> awards;
    private List<String> settings;
    private List<String> characters;

    @NotNull
    private List<String> publishers;

    @NotBlank
    @NotNull
    private String language;

    @NotNull(message = "ISBN must not be null")
    private String isbn;
    private String series;
    private List<String> formats;
    private String coverImgURL;

}