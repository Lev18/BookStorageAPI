package com.bookstore.users.repository;

import com.bookstore.users.entity.User;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("""
            SELECT u from User u
            LEFT JOIN FETCH u.userRole ur
            LEFT JOIN FETCH ur.role r
            WHERE u.email = :email
            """
    )
    Optional<User> findByEmail(@Param("email") String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
