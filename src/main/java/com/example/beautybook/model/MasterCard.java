package com.example.beautybook.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"gallery", "reviews"})
public class MasterCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "address")
    private String address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToOne(
            cascade = CascadeType.ALL,
            mappedBy = "masterCard",
            orphanRemoval = true
    )
    private WorkPeriod workPeriod;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "masterCard",
            orphanRemoval = true
    )
    private Set<Photo> gallery = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "main_photo_id")
    private Photo mainPhoto;
    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "masterCard",
            orphanRemoval = true
    )
    private Set<Review> reviews = new HashSet<>();

    @Column(name = "rating")
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "5.0")
    private BigDecimal rating = new BigDecimal("0.0");

    @Column(name = "description")
    private String description;

    @ManyToMany
    @JoinTable(
            name = "mastercard_subcategory",
            joinColumns = @JoinColumn(name = "mastercard_id"),
            inverseJoinColumns = @JoinColumn(name = "subcategory_id")
    )
    private Set<Subcategory> subcategories;

    @OneToMany(
            mappedBy = "masterCard",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<ServiceCard> serviceCards;

    private String firstName;
    private String lastName;

    public MasterCard(Long id) {
        this.id = id;
    }
}
