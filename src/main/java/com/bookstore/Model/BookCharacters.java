package com.bookstore.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "characters")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookCharacters {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "char_name")
    private String charName; 
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    Book book;
}
