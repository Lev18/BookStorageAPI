package com.bookstore.Model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "formats")
public class Format {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_format")
    private String bookFormat;
    
    @OneToMany(mappedBy = "format")
    List<Book> books;
}
