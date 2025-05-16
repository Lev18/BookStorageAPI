package com.bookstore.books.repository;

import com.bookstore.books.entity.BookGenre;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookGenreRepository extends JpaRepository<BookGenre, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM BookGenre bg where bg.genre.id = :genreId")
    void deleteAllByGenreId(@Param("genreId") Long id);
}
