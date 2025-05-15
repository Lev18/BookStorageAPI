package com.bookstore.books.repository;

import com.bookstore.books.criteria.SearchCriteria;
import com.bookstore.books.entity.Award;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AwardsRepository extends JpaRepository<Award, String> {
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
}
