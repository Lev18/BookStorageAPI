package com.bookstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "series")
@Getter
@Setter
public class Series {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "series_seq")
    @SequenceGenerator(name = "series_seq", sequenceName = "series_seq", allocationSize = 100)
    private  Long id;

    @Column(name = "series")
    private String series;

    @OneToMany(mappedBy = "series")
    private List<Book> books;

    @OneToMany(mappedBy = "series")
    private List<SeriesCharacter> seriesCharacters;

    public Series() {
    }

    public Series(String series) {
        this.series= series;
    }
}
