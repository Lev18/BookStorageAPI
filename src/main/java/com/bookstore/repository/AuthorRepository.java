package com.bookstore.repository;

import com.bookstore.entity.Author;
import com.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
