package com.bookstore.Repository;

import com.bookstore.Model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByGenreTitle(String title);
}
