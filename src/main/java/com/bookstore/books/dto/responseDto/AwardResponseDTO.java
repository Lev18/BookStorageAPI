package com.bookstore.books.dto.responseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AwardResponseDTO {
    private Long awardId;
    private String awardName;
    private Long allBooks;
}
