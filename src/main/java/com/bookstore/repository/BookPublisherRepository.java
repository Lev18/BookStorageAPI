package com.bookstore.repository;

import com.bookstore.entity.BookPublisher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookPublisherRepository extends JpaRepository<BookPublisher, Long> {
}
