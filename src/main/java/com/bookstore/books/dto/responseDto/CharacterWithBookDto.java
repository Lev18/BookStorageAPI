package com.bookstore.books.dto.responseDto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CharacterWithBookDto {
    private Long id;
    private String name;
    private List<BookMiniDto> books = new ArrayList<>();
}
