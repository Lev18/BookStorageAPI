package com.bookstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "book_awards")
@Getter
@Setter
@NoArgsConstructor
public class BookAward {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_award_seq")
    @SequenceGenerator(name = "book_award_seq", sequenceName = "book_award_seq", allocationSize = 100)
    private Long id;

    @Column(name = "book_award")
    String bookAward;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "award_id")
    Award award;

    public BookAward(Book book, Award value, String key) {
        this.book = book;
        this.award = value;
        this.bookAward = key;
    }
}
