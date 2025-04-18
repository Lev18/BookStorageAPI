package com.bookstore.repository;

import com.bookstore.dto.requestDto.AwardDto;
import com.bookstore.entity.Award;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AwardsRepository extends JpaRepository<Award, String> {
    Award findByName(String newAward);
}
