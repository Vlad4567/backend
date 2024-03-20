package com.example.beautybook.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.SQLRestriction;

@Data
@Entity
@EqualsAndHashCode(exclude = "masterCard")
@SQLRestriction("is_deleted=false")
@Table(name = "service_card")
public class ServiceCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    private int duration;

    @ManyToOne
    @JoinColumn(name = "subcategory_id", referencedColumnName = "id")
    private Subcategory subcategory;

    @ManyToOne
    @JoinColumn(name = "master_card_id", referencedColumnName = "id")
    private MasterCard masterCard;

    @OneToOne
    @JoinColumn(name = "photo_id")
    private Photo photo;

    @Column(
            name = "is_deleted",
            nullable = false,
            columnDefinition = "BOOLEAN DEFAULT false"
    )
    private Boolean isDeleted;
}
