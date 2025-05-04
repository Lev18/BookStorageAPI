package com.bookstore.books.mapper;

import com.bookstore.books.entity.Genre;
import com.bookstore.books.dto.csvDto.BookCsvDto;
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
            Genre existingGenre = genresExist.computeIfAbsent(cleanGenre, key-> {
               Genre newGenre = new Genre(cleanGenre);
               allNewGenres.add(newGenre);
               return newGenre;
            });
            genres.add(existingGenre);
        }

        return genres;
    }
}
