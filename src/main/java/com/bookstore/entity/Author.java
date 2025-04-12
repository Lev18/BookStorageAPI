package com.bookstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "authors")
@Getter
@Setter
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_seq")
    @SequenceGenerator(name = "author_seq", sequenceName = "author_seq", allocationSize = 100)
    private Long id;

    @Column(name = "author_name")
    private String authorName;

    @OneToMany(mappedBy = "author")
    private List<BookAuthor> books;

    public Author() {
    }

    public Author(String author) {
        this.authorName=author;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return  true;
        if (obj == null || getClass() !=obj.getClass()) return  false;
        Author author = (Author) obj;
        return Objects.equals(this.authorName, author.authorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorName.trim());
    }
}
