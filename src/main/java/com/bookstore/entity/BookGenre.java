package com.bookstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "book_genres")
@Setter
@Getter
@NoArgsConstructor
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

    public BookGenre(Book book, Genre g) {
        this.book = book;
        this.genre = g;
    }
}
