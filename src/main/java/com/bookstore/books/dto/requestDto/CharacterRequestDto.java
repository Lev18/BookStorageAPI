package com.bookstore.books.dto.requestDto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CharacterRequestDto {
    @NotNull
    private String name;
}
