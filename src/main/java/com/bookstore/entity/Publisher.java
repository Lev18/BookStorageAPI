package com.bookstore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "publishers")
@Setter
@Getter
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "publisher_seq")
    @SequenceGenerator(name = "publisher_seq", sequenceName = "publisher_seq", allocationSize = 100)
    private Long id;

    @Column(name = "publisher_name")
    private String publisherName;

    @OneToMany(mappedBy = "publisher")
    List<BookPublisher> bookPublishers;

    private Publisher(){}

    public Publisher(String publisherName) {
        this.publisherName = publisherName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Publisher genre = (Publisher) obj;
        return Objects.equals(this.publisherName, ((Publisher) obj).publisherName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publisherName);
    }
}
