package com.bookstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "genres")
@Getter
@Setter
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genre_seq")
    @SequenceGenerator(name = "genre_seq", sequenceName = "genre_seq", allocationSize = 100)
    private Long id;

    @Column(name = "genres", nullable = false)
    private String genreTitle;
    
    @OneToMany(mappedBy="genre")
    List<BookGenre> genres;

    private Genre() {}

    public Genre(String genreTitle) {
        this.genreTitle = genreTitle;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Genre genre = (Genre)obj;
        return Objects.equals(this.genreTitle, ((Genre) obj).genreTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genreTitle);
    }

}
