package com.bookstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "series_character")
@Getter
@Setter
@NoArgsConstructor
public class SeriesCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "series_seq")
    @SequenceGenerator(name = "series_seq", sequenceName = "series_seq", allocationSize = 100)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id")
    private Series series;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id")
    private Characters character;

    public SeriesCharacter(Series series, Characters c) {
        this.series = series;
        this.character = c;
    }
}
