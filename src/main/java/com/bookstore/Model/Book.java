package com.bookstore.Model;

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
    public String rating;

    @Column(length = 10485760, name = "description", nullable = false)
    public String description;

    @Column(name = "language", nullable = false)
    public String language;

    @Column(name = "isbn", nullable = false)
    public String isbn;

    @Column(name = "edition")
    public String edition;

    @Column(name = "pages", nullable = false)
    public String pages;

    @Column(name = "publishDate", nullable = false)
    public String publishDate;

    @Column(name = "firstPublishDate")
    public String firstPublishDate;

    @Column(name = "numRatings")
    public Integer numRatings;

    @Column(name = "likedPercent")
    public String likedPercent;

    @Column(name = "coverImg")
    public String coverImg;

    @Column(name = "bbeScore")
    public String bbeScore;

    @Column(name = "bbeVotes")
    public String bbeVotes;

    @Column(name = "price")
    public String price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id") 
    Author author;
   
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "format_id") 
    Format format;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id") 
    Publisher publisher;

    @OneToMany(mappedBy="book")
    List<Awards> awards;

    @OneToMany(mappedBy = "book")
    List<Characters> characters;


    @OneToMany(mappedBy="book")
    List<BookGenre> genres;

    @OneToMany(mappedBy="book")
    List<BookSetting> settings;
}
