package com.bookstore.books.repository;

import com.bookstore.books.entity.BookCharacter;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCharactersRepository extends JpaRepository<BookCharacter, String> {
    @Modifying
    @Transactional
    @Query("DELETE FROM BookCharacter bch where bch.character.id = :charId")
    void deleteAllByGenreId(@Param("charId") Long id);
}
