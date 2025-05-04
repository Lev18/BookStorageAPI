package com.bookstore.books.repository;

import com.bookstore.books.entity.Characters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharactersRepository extends JpaRepository<Characters, String> {
}
