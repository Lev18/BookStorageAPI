package com.bookstore.books.repository;

import com.bookstore.books.entity.BookAward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookAwardRepository extends JpaRepository<BookAward, Long> {
}
