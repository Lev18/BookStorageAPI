package com.bookstore.Repository;

import com.bookstore.Model.Awards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AwardsRepository extends JpaRepository<Awards, String> {
}
