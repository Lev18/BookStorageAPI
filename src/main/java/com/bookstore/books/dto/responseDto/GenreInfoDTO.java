package com.bookstore.books.dto.responseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenreInfoDTO {
    private Long id;
    private String name;
    private Long booksCount;

    public GenreInfoDTO(Long id, String name, Long count) {
        this.id = id;
        this.name = name;
        this.booksCount = count;
    }
}
