package com.bookstore.books.dto.responseDto;

import java.util.List;

public record AwardBookResponseDTO(Long id,
                                   Long book_id,
                                   String awardTitle,
                                   String bookTitle,
                                   String rating,
                                   String bookAward,
                                   List<String> bookAuthor) {
}
