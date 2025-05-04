package com.bookstore.books.repository;

import com.bookstore.books.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileInfo, Long> {
   boolean existsByFileHash(String fileHash);

   @Query("SELECT f from FileInfo f WHERE f.filePath like concat('%', 'small', '%')")
   List<FileInfo> getAllSmallImages();

   @Query("SELECT f from FileInfo f WHERE f.filePath like concat('%', 'original', '%')")
   List<FileInfo> getAllOriginalImages();
}
