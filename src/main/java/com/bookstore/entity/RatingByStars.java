package com.bookstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ratingByStars")
@Setter
@Getter
public class RatingByStars {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rating_seq")
    @SequenceGenerator(name = "rating_seq", sequenceName = "rating_seq", allocationSize = 100)
    private Long id;

    @Column(name="grade")
    private  short grade;

    @Column(name = "stars_rate")
    private Integer ratingStars;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;
}
