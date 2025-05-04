package com.bookstore.books.repository;

import com.bookstore.books.entity.Award;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AwardsRepository extends JpaRepository<Award, String> {
    Award findByName(String newAward);
}
