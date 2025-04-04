package com.bookstore.Repository;

import com.bookstore.Model.RatingByStars;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingByStarsRepository extends JpaRepository<RatingByStars, Long> {
}
