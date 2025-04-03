package com.bookstore.Model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "publishers")
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "publisher_name")
    private String publisherName;
    
    @OneToMany(mappedBy = "publisher")
    List<Book> books;
}
