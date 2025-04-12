package com.bookstore.repository;

import com.bookstore.service.fileReader.FileHash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileHash, Long> {
   boolean existsByFileHash(String fileHash);
}
