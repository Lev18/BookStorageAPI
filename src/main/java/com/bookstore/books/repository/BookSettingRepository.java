package com.bookstore.books.repository;

import com.bookstore.books.entity.BookSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookSettingRepository extends JpaRepository<BookSetting, Long> {
}
