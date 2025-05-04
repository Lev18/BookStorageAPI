package com.bookstore.books.dto.responseDto;

public record BookFlatRecord (Long id,
                              String title,
                              String series,
                              String publishDate,
                              Integer rating,
                              String genre,
                              String author,
                              String publisher,
                              String character,
                              String award)
{
    public BookFlatRecord(Long id,
                          String title,
                          String series,
                          String publishDate,
                          Integer rating,
                          String genre,
                          String author,
                          String publisher,
                          String character,
                          String award) {
        this.id = id;
        this.title = title;
        this.series = series;
        this.publishDate = publishDate;
        this.rating = rating;
        this.genre = genre;
        this.author = author;
        this.publisher = publisher;
        this.character = character;
        this.award = award;
    }
}
