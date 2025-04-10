package com.bookstore.repository;

import com.bookstore.entity.BookCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCharactersRepository extends JpaRepository<BookCharacter, String> {
}
