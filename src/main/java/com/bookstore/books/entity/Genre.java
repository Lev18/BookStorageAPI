package com.bookstore.books.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "genres", nullable = false, unique = true)
    private String name;
    
    @OneToMany(mappedBy="genre")
    List<BookGenre> genres;

    private Genre() {}

    public Genre(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Genre genre = (Genre)obj;
        return Objects.equals(this.name, ((Genre) obj).name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
