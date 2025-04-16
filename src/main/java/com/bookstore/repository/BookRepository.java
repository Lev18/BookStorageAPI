package com.bookstore.repository;

import com.bookstore.entity.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    Book findBookByBookId(String bookId);

    @Query("SELECT b.isbn from Book b")
    Set<String> getAllISBNs();

    @Query("SELECT b.coverImg FROM Book b")
    List<String> getAllUrls();

    @EntityGraph(attributePaths = {
            "genres",
            "genres.genre"
    })
    @Query("SELECT b FROM Book b WHERE b.id = :bookId")
    Book findBookWithGenres(@Param("bookId") String id);

    @EntityGraph(attributePaths = {
            "awards",
            "awards.award"
    })

    @Query("SELECT b FROM Book b WHERE b.id = :bookId")
    Book findBookWithAwards(@Param("bookId") String id);

    @EntityGraph(attributePaths = {
            "genres",
            "genres.genre",
            "awards",
            "awards.award"
    })
    @Query("SELECT b FROM Book b WHERE b.id = :bookId")
    Book findBookWithAttributes(@Param("bookId") String id);

}
