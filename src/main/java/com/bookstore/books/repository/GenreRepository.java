package com.bookstore.books.repository;

import com.bookstore.books.criteria.SearchCriteria;
import com.bookstore.books.entity.Genre;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Genre findByName(String genre);

    @Modifying
    @Transactional
    @Query("DELETE FROM BookGenre bg where bg.genre.id = :genreId")
    void deleteBookGenresByGenreId(@Param("genreId") Long id);

    @Query (value = """
            WITH top_genres AS (select g.id, g.genres
             FROM genres g
             LEFT JOIN\s
             book_genres bg ON bg.genre_id = g.id
             GROUP BY g.id, g.genres
             ORDER BY count(bg.book_id) desc
             LIMIT 5
             )
             SELECT\s
                 tg.id AS genre_id,
                 tg.genres AS genre_name,
                 b.id AS book_id,
                 b.book_title,
                 b.description,
                 b.rating, 
                 b.author_name,
                 b.series_number, 
                 b.publish_date, 
                 b.first_publish_date
             FROM top_genres tg
             JOIN LATERAL (
                 SELECT b.id, b.book_title, b.description, 
                        b.rating, STRING_AGG(at.name,',') AS author_name, 
                        b.series_number, b.publish_date, b.first_publish_date
                 FROM books b
                 JOIN book_genres bg ON b.id = bg.book_id
                 LEFT JOIN book_author bat ON bat.book_id = b.id
                 LEFT JOIN authors at ON bat.author_id = at.id
                 WHERE bg.genre_id = tg.id group by b.id
                 ORDER BY b.id
                 LIMIT 5
             ) b ON true;
            """,
            nativeQuery = true
    )
    Optional<List<Object[]>> findTopFiveGenreFiveBooks();

    @Query(value = """
            SELECT g, COUNT(b) AS bookCount
            FROM Genre g
            LEFT JOIN g.genres.book b
            GROUP BY g
            ORDER BY bookCount DESC
         """
    )
    Optional<Page<Object []>> findAllWithBooksCount(SearchCriteria searchCriteria,
                                                    Pageable pageable);


    @Query(
            value = """
                    SELECT gn.id,
                           gn.genres,
                           b.id,
                           b.book_title,
                           b.description,
                           b.rating,
                           STRING_AGG(at.name, ','),
                           b.series_number,
                           b.publish_date,
                           b.first_publish_date
                    FROM genres gn
                    LEFT JOIN book_genres bgn on bgn.genre_id = gn.id
                    LEFT JOIN books b on bgn.book_id = b.id
                    LEFT JOIN book_author bat on b.id = bat.book_id
                    LEFT JOIN authors at on at.id = bat.author_id 
                    WHERE gn.genres = :genre
                    GROUP BY gn.id,
                           b.id,
                           b.book_title,
                           gn.genres
            """,
            nativeQuery = true
    )
    Optional<Page<Object []>> findWithBooks(@Param(value = "genre") String genre, SearchCriteria searchCriteria,
                                            Pageable pageable);
}
