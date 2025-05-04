package com.bookstore.books.dto.requestDto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AwardDto {
    @NotNull
    private String award;
    @NotNull
    private String awardYear;
}
