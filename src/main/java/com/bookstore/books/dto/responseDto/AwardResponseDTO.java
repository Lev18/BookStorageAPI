package com.bookstore.books.dto.responseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AwardResponseDTO {
    private Long awardId;
    private String awardName;
    private Long allBooks;

    public AwardResponseDTO(Long id, String name, Long i) {
        this.awardId = id;
        this.awardName = name;
        allBooks = i;
    }
}
