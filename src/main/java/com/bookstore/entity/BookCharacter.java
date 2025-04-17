package com.bookstore.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book_character")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_character_seq")
    @SequenceGenerator(name = "book_character_seq", sequenceName = "book_character_seq", allocationSize = 100)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE)
    @JoinColumn(name = "character_id")
    private Characters character;
}
