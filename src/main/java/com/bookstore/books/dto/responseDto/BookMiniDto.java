package com.bookstore.books.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class BookMiniDto {
    private Long id;
    private String title;
    private String description;
    private String awgRating;
    private String publishDate;
    private Set<String> authors;
    private Set<String> series;
}
