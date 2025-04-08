package com.bookstore.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "settings")
public class Setting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "setting")
    private String setting;
    
    @OneToMany(mappedBy="setting")
    private List<BookSetting> settings;    
}
