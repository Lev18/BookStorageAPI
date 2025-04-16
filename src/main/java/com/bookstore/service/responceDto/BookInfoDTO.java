package com.bookstore.service.responceDto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookInfoDTO {
    private String title;
    private String ISBN;
    private List<String> awards;
    private List<String> genres;

    public BookInfoDTO(String title,
                       String isbn,
                       List<String>awards,
                       List<String> genres) {
        this.title = title;
        this.ISBN = isbn;
        this.awards = awards;
        this.genres = genres;
    }
}
