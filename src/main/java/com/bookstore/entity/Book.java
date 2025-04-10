package com.bookstore.entity;

import com.bookstore.service.enums.Language;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "books")
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_secId", nullable = false)
    private String bookId;

    @Column(name = "book_title", nullable = false)
    private String title;

    @Column(name = "book_series")
    private String series;

    @Column(name = "rating")
    private String rating;

    @Column(length = 10485760, name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language;

    @Column(name = "isbn", nullable = false, unique = true  )
    private String isbn;

    @Column(name = "edition")
    private String edition;

    @Column(name = "pages", nullable = false)
    private String pages;

    @Column(name = "publishDate", nullable = false)
    private String publishDate;

    @Column(name = "firstPublishDate")
    private String firstPublishDate;

    @Column(name = "numRatings")
    private Integer numRatings;

    @Column(name = "likedPercent")
    private String likedPercent;

    @Column(name = "coverImg")
    private String coverImg;

    @Column(name = "bbeScore")
    private String bbeScore;

    @Column(name = "bbeVotes")
    private String bbeVotes;

    @Column(name = "price")
    private String price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @OneToMany(mappedBy = "book")
    private List<BookAward> awards;

    @OneToMany(mappedBy = "book")
    private List<BookCharacter> characters;

    @OneToMany(mappedBy = "book")
    private List<BookGenre> genres;

    @OneToMany(mappedBy = "book")
    private List<BookSetting> settings;

    @OneToMany(mappedBy = "ratingStars")
    private List<RatingByStars> stars;

    @OneToMany(mappedBy = "book")
    private List<BookAuthor> author;

    @OneToMany(mappedBy = "book")
    private List<BookFormat> formats;
}
