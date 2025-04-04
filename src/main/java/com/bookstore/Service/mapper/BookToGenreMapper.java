package com.bookstore.Service.mapper;

import com.bookstore.Model.Genre;
import com.bookstore.Repository.GenreRepository;
import com.bookstore.Service.dto.BookCsvDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookToGenreMapper {

    public List<Genre> findOrCreateGenre(BookCsvDto bookCsvDto, GenreRepository genreRepository) {
        List<Genre> genres = new ArrayList<>();
        for (String title : bookCsvDto.getGenres()) {
            Genre genre = genreRepository.findByGenreTitle(title)
                    .orElseGet(()-> new Genre(title));
            genres.add(genre);
        }
        return genres;
    }
}
