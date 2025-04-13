package com.bookstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "book_publisher")
@Getter
@Setter
public class BookPublisher {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_publisher_seq")
    @SequenceGenerator(name = "book_publisher_seq", sequenceName = "book_publisher_seq", allocationSize = 100)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;
}
