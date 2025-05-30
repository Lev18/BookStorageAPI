package com.bookstore.users.repository;

import com.bookstore.users.entity.UserRole;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM UserRole ur where ur.user.id = :userId")
    void deleteByUserId(@Param("userId") Long id);
}
