package com.bookstore.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "genres")
@Getter
@Setter
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "genres", nullable = false)
    private String genreTitle;
    
    @OneToMany(mappedBy="genre")
    List<BookGenre> genres;

    public Genre() {}

    public Genre(String genreTitle) {
        this.genreTitle = genreTitle;
    }

}
