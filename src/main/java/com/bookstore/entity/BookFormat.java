package com.bookstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "book_format")
@Getter
@Setter
public class BookFormat {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_format_seq")
    @SequenceGenerator(name = "book_format_seq", sequenceName = "book_format_seq", allocationSize = 100)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "format_id")
    private Format format;
}
