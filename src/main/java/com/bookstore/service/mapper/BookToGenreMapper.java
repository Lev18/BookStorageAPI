package com.bookstore.service.mapper;

import com.bookstore.entity.Genre;
import com.bookstore.repository.GenreRepository;
import com.bookstore.service.dto.BookCsvDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class BookToGenreMapper {

    public List<Genre> findOrCreateGenre(BookCsvDto bookCsvDto, Set<Genre> genresInRepository) {
        List<Genre> genres = new ArrayList<>();
        for (String genre : bookCsvDto.getGenres()) {
            Genre newgenre = new Genre(genre);
            if (!genresInRepository.contains(newgenre)) {
                genres.add(newgenre);
            }
        }
        genresInRepository.addAll(genres);
        return genres;
    }
}
