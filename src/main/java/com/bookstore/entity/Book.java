package com.bookstore.entity;

import com.bookstore.service.enums.Language;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "books")
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_seq")
    @SequenceGenerator(name = "book_seq", sequenceName = "book_seq", allocationSize = 100)
    private Long id;

    @Column(name = "book_secId", nullable = false)
    private String bookId;

    @Column(name = "book_title", nullable = false)
    private String title;

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

    @OneToMany(mappedBy = "book")
    private List<BookPublisher> bookPublishers;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private Set<BookAward> awards;

    @OneToMany(mappedBy = "book")
    private List<BookCharacter> characters;

    @OneToMany(mappedBy = "book")
    private Set<BookGenre> genres;

    @OneToMany(mappedBy = "book")
    private List<BookSetting> settings;

    @OneToMany(mappedBy = "ratingStars")
    private List<RatingByStars> stars;

    @OneToMany(mappedBy = "book")
    private List<BookAuthor> author;

    @OneToMany(mappedBy = "book")
    private List<BookFormat> formats;

    @OneToMany(mappedBy = "book")
    private List<BookSetting> bookSettings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id")
    private Series series;

    public void setNumRatingByStars(List<RatingByStars> ratingByStars) {
      this.numRatings = ratingByStars.stream()
              .mapToInt(RatingByStars::getRatingStars)
              .sum();
    }

}
