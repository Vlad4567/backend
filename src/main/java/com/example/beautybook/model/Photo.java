package com.example.beautybook.model;

import com.example.beautybook.annotetion.OnlyIdToString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "photos")
@AllArgsConstructor
@NoArgsConstructor
public class Photo extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url", nullable = false)
    private String photoUrl;

    @OnlyIdToString
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_card_id")
    private MasterCard masterCard;

    private Long subcategoryId;

    @Column(
            name = "is_main",
            nullable = false,
            columnDefinition = "BOOLEAN DEFAULT false"
    )
    private boolean isMain;

    public Photo(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return super.toString(this);
    }
}
