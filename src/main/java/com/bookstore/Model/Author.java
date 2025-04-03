package com.bookstore.Model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "author_name")
    private String authorName;

    @OneToMany(mappedBy = "author")
    List<Book> books;
}
