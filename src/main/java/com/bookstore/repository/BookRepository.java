package com.bookstore.repository;

import com.bookstore.entity.Book;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    Book findBookByBookId(String bookId);

    Book findBookByIsbn(String bookIsbn);

    @Query("SELECT b.isbn from Book b")
    Set<String> getAllISBNs();

    @Query("SELECT b.coverImg FROM Book b")
    List<String> getAllUrls();

    //Replace by ISBN
    @Query("SELECT b.coverImg FROM Book b WHERE b.isbn = :bookIsbn")
    String getUrlById(@Param("bookIsbn") String isbn);

    @EntityGraph(attributePaths = {
            "genres",
            "genres.genre",
            "awards",
            "awards.award",
            "characters",
            "characters.character",
            "author",
            "author.author",
    })
    @Query("SELECT b FROM Book b WHERE b.isbn = :bookIsbn")
    Book findBookWithAttributes(@Param("bookIsbn") String isbn);


    @Modifying
    @Query("DELETE FROM BookSetting bs WHERE bs.book.isbn = :bookIsbn")
    @Transactional
    void deleteBookSettingsByBookIsbn(@Param("bookIsbn") String bookIsbn);

    @Modifying
    @Query("DELETE FROM BookPublisher bp WHERE bp.book.isbn = :bookIsbn")
    @Transactional
    void deleteBookPublisherByBookIsbn(@Param("bookIsbn") String bookIsbn);

    @Modifying
    @Query("DELETE FROM RatingByStars rbs WHERE rbs.book.isbn = :bookIsbn")
    @Transactional
    void deleteRatingByStarsByBookIsbn(@Param("bookIsbn") String bookIsbn);

    @Modifying
    @Query("DELETE FROM BookGenre bg WHERE bg.book.isbn = :bookIsbn")
    @Transactional
    void deleteBookGenreByBookIsbn(@Param("bookIsbn") String bookIsbn);

    @Modifying
    @Query("DELETE FROM BookAward ba WHERE ba.book.isbn = :bookIsbn")
    @Transactional
    void deleteBookAwardByBookIsbn(@Param("bookIsbn") String bookIsbn);

    @Modifying
    @Query("DELETE FROM BookAuthor bat WHERE bat.book.isbn = :bookIsbn")
    @Transactional
    void deleteBookAuthorByBookIsbn(@Param("bookIsbn") String bookIsbn);

    @Modifying
    @Query("DELETE FROM BookFormat bf WHERE bf.book.isbn = :bookIsbn")
    @Transactional
    void deleteBookFormatByBookIsbn(@Param("bookIsbn") String bookIsbn);

    @Modifying
    @Query("DELETE FROM BookCharacter bch WHERE bch.book.isbn = :bookIsbn")
    @Transactional
    void deleteBookCharacterByBookIsbn(@Param("bookIsbn") String bookIsbn);

    @Modifying
    @Query("DELETE FROM Book b WHERE b.isbn = :bookIsbn")
    @Transactional
    void deleteBookByISBN(@Param("bookIsbn") String bookIsbn);

}
