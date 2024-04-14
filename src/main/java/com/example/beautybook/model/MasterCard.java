package com.example.beautybook.model;

import com.example.beautybook.annotetion.IgnoreToSting;
import com.example.beautybook.annotetion.OnlyIdToString;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@NoArgsConstructor
@SQLRestriction("is_deleted=false")
public class MasterCard extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OnlyIdToString
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OnlyIdToString
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    @ToString.Exclude
    private Address address;

    @IgnoreToSting
    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "masterCard",
            orphanRemoval = true
    )
    private Set<Photo> gallery = new HashSet<>();

    @OnlyIdToString
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_photo_id")
    private Photo mainPhoto;

    @Column(name = "rating")
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "5.0")
    private BigDecimal rating = new BigDecimal("0.0");

    @Column(name = "description")
    private String description;

    @IgnoreToSting
    @ManyToMany
    @JoinTable(
            name = "mastercard_subcategory",
            joinColumns = @JoinColumn(name = "mastercard_id"),
            inverseJoinColumns = @JoinColumn(name = "subcategory_id")
    )
    private Set<Subcategory> subcategories;

    @IgnoreToSting
    @OneToMany(
            mappedBy = "masterCard",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<ServiceCard> serviceCards;

    private String firstName;
    private String lastName;

    @OnlyIdToString
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contacts_id")
    private Contacts contacts;

    @Column(
            name = "is_hidden",
            nullable = false,
            columnDefinition = "BOOLEAN DEFAULT false"
    )
    private boolean isHidden;

    @Column(
            name = "is_deleted",
            nullable = false,
            columnDefinition = "BOOLEAN DEFAULT false"
    )
    private boolean isDeleted;

    public MasterCard(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return super.toString(this);
    }
}
