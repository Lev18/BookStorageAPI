package com.bookstore.dto.responseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenreInfoDTO {
    private String title;
    private String genre;
    private String ISBN;

    public GenreInfoDTO(String title, String genre, String isbn) {
        this.title = title;
        this.genre = genre;
        this.ISBN = isbn;
    }
}
