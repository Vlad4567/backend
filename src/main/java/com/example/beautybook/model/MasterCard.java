package com.example.beautybook.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(exclude = "gallery")
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
    private String profilePhoto;
    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "masterCard",
            orphanRemoval = true
    )
    private Set<Photo> gallery = new HashSet<>();
    @Column(name = "description")
    private String description;
}
