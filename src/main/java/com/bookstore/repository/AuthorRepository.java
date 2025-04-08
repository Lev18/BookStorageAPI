package com.bookstore.Repository;

import com.bookstore.Model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
   Optional<Author> findByAuthorName(String author);
}
