package com.bookstore.Repository;

import com.bookstore.Model.Format;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FormatRepository extends JpaRepository<Format, String> {
    Optional<Format> findByBookFormat(String format);
}
