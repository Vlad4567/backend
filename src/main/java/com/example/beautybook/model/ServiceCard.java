package com.example.beautybook.model;

import com.example.beautybook.annotetion.OnlyIdToString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLRestriction;

@Setter
@Getter
@Entity
@SQLRestriction("is_deleted=false")
@Table(name = "service_card")
public class ServiceCard extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    private int duration;

    @OnlyIdToString
    @ManyToOne
    @JoinColumn(name = "subcategory_id", referencedColumnName = "id")
    private Subcategory subcategory;

    @OnlyIdToString
    @ManyToOne
    @JoinColumn(name = "master_card_id", referencedColumnName = "id")
    @ToString.Exclude
    private MasterCard masterCard;

    @OnlyIdToString
    @ManyToOne
    @JoinColumn(name = "photo_id")
    private Photo photo;

    @Column(
            name = "is_deleted",
            nullable = false,
            columnDefinition = "BOOLEAN DEFAULT false"
    )
    private boolean isDeleted;

    @Override
    public String toString() {
        return super.toString(this);
    }
}
