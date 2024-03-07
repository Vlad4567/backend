package com.example.beautybook.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "categories")
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(
            name = "name",
            unique = true,
            nullable = false
    )
    private String name;
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "category")
    private List<Subcategory> subcategories;

    public Category(Long id) {
        this.id = id;
    }
}
