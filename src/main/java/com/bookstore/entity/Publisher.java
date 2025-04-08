package com.bookstore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "publishers")
@Setter
@Getter
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "publisher_name")
    private String publisherName;
    
    @OneToMany(mappedBy = "publisher")
    List<Book> books;
    private Publisher(){}

    public Publisher(String publisherName) {
        this.publisherName = publisherName;
    }
}
