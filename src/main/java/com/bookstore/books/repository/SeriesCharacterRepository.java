package com.bookstore.books.repository;

import com.bookstore.books.entity.SeriesCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeriesCharacterRepository extends JpaRepository<SeriesCharacter, Long> {
}
