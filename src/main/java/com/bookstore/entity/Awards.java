// many to one
package com.bookstore.entity;

import jakarta.persistence.*;
import lombok.Setter;

@Entity
@Table(name = "awards")
@Setter
public class Awards {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "award_title")
    private String awardTitle; 
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

}
