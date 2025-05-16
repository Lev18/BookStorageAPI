package com.bookstore.books.repository;

import com.bookstore.books.criteria.SearchCriteria;
import com.bookstore.books.dto.responseDto.CharacterResponseDto;
import com.bookstore.books.entity.Characters;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharactersRepository extends JpaRepository<Characters, Long> {
    boolean existsByName(String name);
    @Query("SELECT c FROM Characters c")
    Optional<Page<Characters>> findAllCharacters(SearchCriteria searchCriteria,
                                                 Pageable pageable);
    @Query( value = """
                    SELECT ch.id,
                           ch.name,
                           b.id,
                           b.book_title,
                           b.description,
                           b.rating,
                           STRING_AGG(at.name, ','),
                           b.series_number,
                           b.publish_date,
                           b.first_publish_date
                    FROM characters ch
                    LEFT JOIN book_character bch on bch.character_id = ch.id
                    LEFT JOIN books b on bch.book_id = b.id
                    LEFT JOIN book_author bat on b.id = bat.book_id
                    LEFT JOIN authors at on at.id = bat.author_id 
                    WHERE ch.id = :id
                    GROUP BY ch.id,
                           b.id,
                           b.book_title,
                           ch.name
            """,
            nativeQuery = true
    )
    Optional<List<Object []>> findCharacterWithBookById(@Param(value = "id") Long id);
}
