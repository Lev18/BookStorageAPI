package com.bookstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "book_author")
@Getter
@Setter
public class BookAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_author_seq")
    @SequenceGenerator(name = "book_author_seq", sequenceName = "book_author_seq", allocationSize = 100)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    Author author;
}
