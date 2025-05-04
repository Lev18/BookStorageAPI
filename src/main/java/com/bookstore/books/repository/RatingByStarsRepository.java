package com.bookstore.books.repository;

import com.bookstore.books.entity.Book;
import com.bookstore.books.entity.RatingByStars;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingByStarsRepository extends JpaRepository<RatingByStars, Long> {
    List<RatingByStars> findAllByBook(Book b);
}
