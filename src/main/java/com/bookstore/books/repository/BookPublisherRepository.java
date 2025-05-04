package com.bookstore.books.repository;

import com.bookstore.books.entity.BookPublisher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookPublisherRepository extends JpaRepository<BookPublisher, Long> {
}
