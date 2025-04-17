package com.bookstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "book_genres")
@Setter
@Getter
public class BookGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_genre_seq")
    @SequenceGenerator(name = "book_genre_seq", sequenceName = "book_genre_seq", allocationSize = 100)
    private Long id;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE)
    @JoinColumn(name = "genre_id")
    private Genre genre; 
}
