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
{ }
