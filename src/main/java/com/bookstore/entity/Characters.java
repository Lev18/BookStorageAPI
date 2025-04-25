package com.bookstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "characters")
@Getter
@Setter
public class Characters {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "character_seq")
    @SequenceGenerator(name = "character_seq", sequenceName = "character_seq", allocationSize = 100)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "character")
    private List<BookCharacter> bookCharacters;

    @OneToMany(mappedBy = "character")
    private List<SeriesCharacter> seriesCharacters;

    public Characters() {
    }

    public Characters(String character) {
        this.name= character;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return  true;
        if (obj == null || getClass() !=obj.getClass()) return  false;
        Characters character = (Characters) obj;
        return Objects.equals(this.name, character.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.trim());
    }
}
