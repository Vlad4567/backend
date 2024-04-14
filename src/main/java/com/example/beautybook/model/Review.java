package com.example.beautybook.model;

import com.example.beautybook.annotetion.OnlyIdToString;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "reviews")
public class Review extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OnlyIdToString
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Min(0)
    @Max(5)
    private int grade;

    private String comment;

    @OnlyIdToString
    @ManyToOne
    @JoinColumn(name = "master_card_id")
    private MasterCard masterCard;

    private LocalDateTime dateTime = LocalDateTime.now();

    @Override
    public String toString() {
        return super.toString(this);
    }
}
