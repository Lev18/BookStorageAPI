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

    @Column(name = "character_name")
    private String characterName;

    @OneToMany(mappedBy = "character")
    private List<BookCharacter> bookCharacters;

    public Characters() {
    }

    public Characters(String character) {
        this.characterName= character;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return  true;
        if (obj == null || getClass() !=obj.getClass()) return  false;
        Characters character = (Characters) obj;
        return Objects.equals(this.characterName, character.characterName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterName.trim());
    }
}
