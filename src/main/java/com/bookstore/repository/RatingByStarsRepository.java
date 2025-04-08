package com.bookstore.repository;

import com.bookstore.entity.RatingByStars;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingByStarsRepository extends JpaRepository<RatingByStars, Long> {
}
