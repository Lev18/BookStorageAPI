package com.bookstore.service.mapper;

import com.bookstore.entity.Genre;
import com.bookstore.repository.GenreRepository;
import com.bookstore.service.dto.BookCsvDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
@Transactional
public class BookToGenreMapper {
    private final GenreRepository genreRepository;
    public List<Genre> findOrCreateGenre(BookCsvDto bookCsvDto, Set<Genre> genresInRepository) {
        List<Genre> genres = new ArrayList<>();
        List<Genre> new_genres = new ArrayList<>();
        for (String genre : bookCsvDto.getGenres()) {
            Genre newgenre = new Genre(genre);

            Genre existingGenre = genresInRepository.stream()
                                                    .filter(genre1 -> genre1.getGenreTitle().equals(genre))
                                                    .findFirst()
                                                    .orElse(null);
            if (existingGenre != null) {
                genres.add(existingGenre);
            } else {
                new_genres.add(newgenre);
                genres.add(newgenre);
            }
        }

        if (!genres.isEmpty()) {
            genreRepository.saveAll(new_genres);
            genresInRepository.addAll(new_genres);
        }
        return genres;
    }
}
