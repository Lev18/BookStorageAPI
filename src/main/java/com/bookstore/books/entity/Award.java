package com.bookstore.books.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "awards")
@Setter
@Getter
@NoArgsConstructor
public class Award {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "award_seq")
    @SequenceGenerator(name = "award_seq", sequenceName = "award_seq", allocationSize = 100)
    private Long id;

    @Column(name = "award")
    private String name;

    @OneToMany(mappedBy = "award",
            fetch = FetchType.LAZY)
    private List<BookAward> awards;

    public Award(String award) {this.name = award;}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return  true;
        if (obj == null || getClass() !=obj.getClass()) return  false;
        Award format = (Award) obj;
        return Objects.equals(this.name, format.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.trim());
    }
}
