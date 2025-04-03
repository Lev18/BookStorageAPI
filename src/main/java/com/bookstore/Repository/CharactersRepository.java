package com.bookstore.Repository;

import com.bookstore.Model.BookCharacters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharactersRepository extends JpaRepository<BookCharacters, String> {

}
