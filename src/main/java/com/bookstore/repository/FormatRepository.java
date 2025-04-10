package com.bookstore.repository;

import com.bookstore.entity.Format;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FormatRepository extends JpaRepository<Format, String> {
   // Optional<Format> findByBookFormat(String format);
}
