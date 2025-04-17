package com.bookstore.repository;

import com.bookstore.entity.Genre;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Genre findByGenreTitle(String genre);

    @Modifying
    @Transactional
    @Query("DELETE FROM BookGenre bg where bg.genre.id = :genreId")
    void deleteBookGenresByGenreId(@Param("genreId") Long id);
}
