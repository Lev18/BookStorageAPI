package com.bookstore.books.repository;

import com.bookstore.books.entity.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookAuthorRepository extends JpaRepository<BookAuthor, Long> {
}
