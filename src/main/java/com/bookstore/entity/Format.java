package com.bookstore.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "formats")
@NoArgsConstructor
public class Format {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_format")
    private String bookFormat;
    
    @OneToMany(mappedBy = "format", cascade = CascadeType.ALL)
    List<Book> books;
    public  Format(String formatName) {
        this.bookFormat = formatName;
    }

}
