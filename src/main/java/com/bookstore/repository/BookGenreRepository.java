package com.bookstore.Repository;

import com.bookstore.Model.BookGenre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookGenreRepository extends JpaRepository<BookGenre, Long> {

}
