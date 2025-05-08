package com.bookstore.books.repository;

import com.bookstore.books.criteria.BookSearchCriteria;
import com.bookstore.books.criteria.SearchCriteria;
import com.bookstore.books.dto.responseDto.BookFlatRecord;
import com.bookstore.books.entity.Book;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    Book findBookByBookId(String bookId);

    Book findBookByIsbn(String bookIsbn);

    @Query("SELECT b.isbn from Book b")
    Set<String> getAllISBNs();

    @Query("SELECT b.coverImg FROM Book b")
    List<String> getAllUrls();

    // Replace by ISBN
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
            "bookSettings",
            "bookSettings.setting"
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

//    String genre,
//    String author,
//    String publisher,
//    String award
    @Query("""
            SELECT DISTINCT new com.bookstore.books.dto.responseDto.BookFlatRecord(
                    b.id,
                    b.title,
                    b.seriesNumber,
                    b.publishDate,
                    b.numRatings,
                    g.genre.name,
                    aut.author.name,
                    p.publisher.name,
                    ch.character.name,
                    aw.award.name
                    )
            FROM Book  b
                LEFT JOIN b.author aut
                LEFT JOIN b.genres g
                LEFT JOIN b.bookPublishers p
                LEFT JOIN b.awards aw
                LEFT JOIN b.characters ch
            WHERE (:#{#criteria.genre} IS NULL OR g.genre.name LIKE CONCAT('%', :#{#criteria.genre}, '%'))
                AND (:#{#criteria.publisher} IS NULL OR p IS NULL OR p.publisher.name LIKE CONCAT('%', :#{#criteria.publisher},'%'))
                AND (:#{#criteria.author} IS NULL OR aut IS NULL OR aut.author.name LIKE CONCAT('%', :#{#criteria.author}, '%'))
                AND(:#{#criteria.award} IS NULL OR aw IS NULL OR aw.award.name LIKE CONCAT('%', :#{#criteria.award}, '%'))
                AND(:#{#criteria.character} IS NULL OR aw IS NULL OR ch.character.name LIKE CONCAT('%', :#{#criteria.character}, '%'))
            GROUP BY  b.id,
                    b.title,
                    b.seriesNumber,
                    g.genre.name,
                    aut.author.name,
                    p.publisher.name,
                    ch.character.name,
                    aw.award.name
     """
    )
    List<BookFlatRecord> findAll(BookSearchCriteria criteria);

    @Query(value = """
            SELECT  DISTINCT b.id,
                       b.title,
                       b.isbn,
                       b.seriesNumber,
                       b.publishDate,
                       b.numRatings,
                    STRING_AGG(g.genre.name, ','),
                    STRING_AGG(aut.author.name, ','),
                    STRING_AGG(p.publisher.name, ',') ,
                    STRING_AGG(ch.character.name, ','),
                    STRING_AGG(aw.award.name, ',')
            FROM Book  b
                LEFT JOIN b.author aut
                LEFT JOIN b.genres g
                LEFT JOIN b.bookPublishers p
                LEFT JOIN b.awards aw
                LEFT JOIN b.bookSettings st
                LEFT JOIN b.characters ch
        WHERE (:#{#criteria.genre} IS NULL OR g IS NULL OR g.genre.name LIKE CONCAT('%', :#{#criteria.genre}, '%'))
                AND (:#{#criteria.publisher} IS NULL OR p IS NULL OR p.publisher.name LIKE CONCAT('%', :#{#criteria.publisher},'%'))
                AND (:#{#criteria.author} IS NULL OR aut IS NULL OR aut.author.name LIKE CONCAT('%', :#{#criteria.author}, '%'))
                AND(:#{#criteria.award} IS NULL OR aw IS NULL OR aw.award.name LIKE CONCAT('%', :#{#criteria.award}, '%'))
                AND(:#{#criteria.character} IS NULL OR ch IS NULL OR ch.character.name LIKE CONCAT('%', :#{#criteria.character}, '%'))
        GROUP BY  b.id, b.title, b.isbn, b.seriesNumber, b.publishDate, b.numRatings
     """
    )
    Page<Object []> findAllWithFilters(SearchCriteria criteria,
                                       Pageable pageable);


    @Query(value = """
            SELECT  DISTINCT b.id,
                       b.book_title,
                       b.isbn,
                       b.series_number,
                       b.publish_date,
                       b.num_ratings,
                       STRING_AGG(gn.genres, ','),
                       STRING_AGG(at.name, ','),
                       STRING_AGG(p.publisher_name, ','),
                       STRING_AGG(c2_0.name, ','),
                       STRING_AGG(aw.award, ','),
                       STRING_AGG(st.name, ',')
            FROM books  b
                       left join book_publisher bp
                            on bp.book_id = b.id
                       left join publishers p
                            on bp.publisher_id = p.id  
                       left join book_author bat
                            on bat.book_id = b.id
                       left join authors at
                            on bat.author_id = at.id
                       left join book_genres bgn
                            on bgn.book_id = b.id
                       left join genres gn
                            on bgn.genre_id = gn.id
                       left join book_awards baw
                            on baw.book_id = b.id
                       left join awards aw
                            on baw.award_id = aw.id         
                       left join book_character c1_0
                             on b.id=c1_0.book_id
                       left join characters c2_0
                              on c2_0.id=c1_0.character_id
                       left join book_settings bst
                              on b.id = bst.book_id
                       left join settings st
                              on st.id = bst.setting_id
            WHERE (COALESCE(:#{#criteria.genre}, '') = '' OR 
                            gn.genres LIKE CONCAT('%', CAST(:#{#criteria.genre} AS VARCHAR), '%'))
                AND (COALESCE(:#{#criteria.publisher}, '') = '' OR 
                            p.publisher_name LIKE CONCAT('%', CAST(:#{#criteria.publisher} AS VARCHAR),'%'))
                AND (COALESCE(:#{#criteria.author}, '') = '' OR 
                            at.name LIKE CONCAT('%', CAST(:#{#criteria.author} AS VARCHAR), '%'))
                AND (COALESCE(:#{#criteria.award}, '') = '' OR 
                            aw.award LIKE CONCAT('%', CAST(:#{#criteria.award} AS VARCHAR), '%'))
                AND (COALESCE(:#{#criteria.character}, '') = '' OR
                            c2_0.name LIKE CONCAT('%', CAST(:#{#criteria.character} AS VARCHAR), '%'))
            GROUP BY  b.id, b.book_title, b.isbn
     """, nativeQuery = true
    )
    Page<Object []> findAllWithNative(SearchCriteria criteria,
                                       Pageable pageable);
}
































//SELECT b.id, b.title, b.seriesNumber, b.publishDate
//FROM books  b
//LEFT JOIN book_genres bg on bg.book_id = b.id
//LEFT JOIN genres g on bg.genre_id = g.id
//LEFT JOIN b.genres g
//LEFT JOIN b.bookPublishers p
//LEFT JOIN b.awards aw
//WHERE (:#{#criteria.genre} IS NULL OR g.genre.name LIKE CONCAT('%', :#{#criteria.genre}, '%'))
//AND (:#{#criteria.publisher} IS NULL OR p.publisher.name LIKE CONCAT('%', :#{#criteria.publisher},'%'))
//AND (:#{#criteria.author} IS NULL OR aut.author.name LIKE CONCAT('%', :#{#criteria.author}, '%'))
//AND(:#{#criteria.award} IS NULL OR aw.award.name LIKE CONCAT('%', :#{#criteria.award}, '%'))

//3956203 | adult fiction  | viking | Ruth Ozeki (Goodreads Author)   andrew carnegie medal nominee







