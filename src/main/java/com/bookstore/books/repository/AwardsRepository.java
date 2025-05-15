package com.bookstore.books.repository;

import com.bookstore.books.criteria.SearchCriteria;
import com.bookstore.books.entity.Award;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AwardsRepository extends JpaRepository<Award, Long> {
    Award findByName(String newAward);

    @Query(value = """
            SELECT a, COUNT(b) AS bookCount
            FROM Award a
            LEFT JOIN a.awards.book b
            GROUP BY a
            ORDER BY bookCount DESC
         """
    )
    Optional<Page<Object []>> findAllWithBooks(SearchCriteria searchCriteria, Pageable pageable);

    @Query(
            value = """
                    SELECT aw.id,
                           b.id,
                           aw.award,
                           b.book_title,
                           b.rating,
                           baw.book_award,
                           STRING_AGG(at.name, ',')
                    FROM awards aw
                    LEFT JOIN book_awards baw on baw.award_id = aw.id
                    LEFT JOIN books b on baw.book_id = b.id
                    LEFT JOIN book_author bat on b.id = bat.book_id
                    LEFT JOIN authors at on at.id = bat.author_id 
                    WHERE aw.id = :id
                    GROUP BY aw.id,
                           b.id,
                           b.book_title,
                           aw.award, 
                           baw.book_award
            """,
            nativeQuery = true
    )
    Optional<Page<Object []>> findWithBoos(@Param(value = "id") Long id, SearchCriteria searchCriteria,
                                           Pageable pageable);
}
