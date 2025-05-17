package com.bookstore.users.repository;

import com.bookstore.users.entity.UserPermission;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermission, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM UserPermission up where up.user.id = :userId")
    void deleteByUserId(@Param("userId") Long id);
}
