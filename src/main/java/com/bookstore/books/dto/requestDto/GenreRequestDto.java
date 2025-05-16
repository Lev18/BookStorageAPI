package com.bookstore.books.dto.requestDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class GenreRequestDto {
    @NotNull
    private String name;
}
