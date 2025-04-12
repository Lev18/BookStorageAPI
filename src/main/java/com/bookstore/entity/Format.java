package com.bookstore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "formats")
@Setter
@Getter
@NoArgsConstructor
public class Format {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "format_seq")
    @SequenceGenerator(name = "format_seq", sequenceName = "format_seq", allocationSize = 100)
    private Long id;

    @Column(name = "format")
    private String format;

    @OneToMany(mappedBy = "format", fetch = FetchType.LAZY)
    private List<BookFormat> formats;

    public Format(String format) {this.format = format;}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return  true;
        if (obj == null || getClass() !=obj.getClass()) return  false;
        Format format = (Format) obj;
        return Objects.equals(this.format, format.format);
    }

    @Override
    public int hashCode() {
        return Objects.hash(format.trim());
    }
}
