package com.bookstore.repository;

import com.bookstore.entity.BookFormat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookFormatRepository extends JpaRepository<BookFormat, Long> {
}
