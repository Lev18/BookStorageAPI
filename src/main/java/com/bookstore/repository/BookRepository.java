package com.bookstore.repository;

import com.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    Book findBookByBookId(String bookId);

    @Query("SELECT b.isbn from Book b")
    Set<String> getAllISBNs();
}
