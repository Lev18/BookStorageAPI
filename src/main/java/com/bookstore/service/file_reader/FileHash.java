package com.bookstore.Service.FileReader;

import jakarta.persistence.*;

@Entity
public class FileHash {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "hashed")
    private String fileHash;

    public FileHash() {
    }

    public FileHash(String hash) {
        this.fileHash = hash;
    }

    public FileHash setHash(String hash) {
        this.fileHash = hash;
        return this;
    }
}
