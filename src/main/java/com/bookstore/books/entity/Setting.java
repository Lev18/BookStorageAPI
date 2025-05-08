package com.bookstore.books.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "settings")
@Getter
@Setter
@NoArgsConstructor
public class Setting {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "setting_seq")
    @SequenceGenerator(name = "setting_seq", sequenceName = "setting_seq", allocationSize = 100)
    private Long id;

    @Column(name = "name")
    private String name;
    
    @OneToMany(mappedBy="setting",
            cascade = CascadeType.REMOVE)
    private List<BookSetting> bookSettings;

    public Setting(String setting) {
        this.name = setting;
    }

}
