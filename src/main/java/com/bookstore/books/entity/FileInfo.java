package com.bookstore.books.entity;

import com.bookstore.books.enums.FileDownloadStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "file_info")
@Getter
@Setter
public class FileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_info_seq")
    @SequenceGenerator(name = "file_info_seq", sequenceName = "file_info_seq", allocationSize = 100)
    private Long id;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_format")
    private String fileFormat;

    @Enumerated(value = EnumType.STRING)
    private FileDownloadStatus fileDownloadStatus;

    @Column(name = "error_message", length = 10485760)
    private String errorMessage;

    @Column(name = "hashed")
    private String fileHash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "book_id")
    private Book book;

    public FileInfo() {
    }

    public FileInfo(String hash) {
        this.fileHash = hash;
    }

    public FileInfo setHash(String hash) {
        this.fileHash = hash;
        return this;
    }

}
