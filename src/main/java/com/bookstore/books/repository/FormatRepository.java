package com.bookstore.books.repository;

import com.bookstore.books.entity.Format;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormatRepository extends JpaRepository<Format, String> {
   // Optional<Format> findByBookFormat(String format);
}
