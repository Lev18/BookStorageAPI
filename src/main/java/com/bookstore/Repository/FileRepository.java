package com.bookstore.Repository;

import com.bookstore.Service.FileReader.FileHash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileHash, Long> {
   boolean existsByFileHash(String fileHash);
}
