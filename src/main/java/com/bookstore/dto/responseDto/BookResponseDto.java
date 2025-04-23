package com.bookstore.dto.responseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BookResponseDto {
    private Long id;
    private String title;
    private String series;
    private String publishDate;
    private Double awgRating;
    private Integer rating;
    private List<String> author;

    public BookResponseDto(Long id,
                           String title,
                           String series,
                           String publishDate) {
        this.id = id;
        this.title = title;
        this.series = series;
        this.publishDate = publishDate;
    }
}
