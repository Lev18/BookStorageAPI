package com.bookstore.books.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "book_settings")
@Getter
@Setter
@NoArgsConstructor
public class BookSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "book_setting_seq")
    @SequenceGenerator(name = "book_setting_seq",
            sequenceName = "book_setting_seq",
            allocationSize = 100)
    private Long id;
 
    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setting_id")
    private Setting setting;

    public BookSetting(Book book, Setting s) {
        this.book = book;
        this.setting = s;
    }
}
