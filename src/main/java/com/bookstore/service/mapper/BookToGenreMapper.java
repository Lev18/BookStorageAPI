package com.bookstore.service.mapper;

import com.bookstore.entity.Genre;
import com.bookstore.dto.csvDto.BookCsvDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class BookToGenreMapper {
    // it is service logic
//    private final GenreRepository genreRepository;
    public List<Genre> findOrCreateGenre(BookCsvDto bookCsvDto,
                                         // rename
                                         Map<String, Genre> genresExist,
                                         List<Genre> allNewGenres) {
        List<Genre> genres = new ArrayList<>();

        for (String genre : bookCsvDto.getGenres()) {
            String cleanGenre = genre.replaceAll("'","").trim().toLowerCase();

            Genre newGenre = new Genre(cleanGenre);
            Genre existingGenre = genresExist.get(cleanGenre);

            if (existingGenre != null) {
                genres.add(existingGenre);
            } else {
                allNewGenres.add(newGenre);
                genres.add(newGenre);
                genresExist.put(cleanGenre, newGenre);
            }
        }

        return genres;
    }
}
