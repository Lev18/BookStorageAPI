package com.bookstore.books.repository;

import com.bookstore.books.entity.BookFormat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookFormatRepository extends JpaRepository<BookFormat, Long> {
}
