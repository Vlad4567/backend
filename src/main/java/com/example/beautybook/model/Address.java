package com.example.beautybook.model;

import com.example.beautybook.annotetion.OnlyIdToString;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "Addresses")
public class Address extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OnlyIdToString
    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;
    private String street;
    private String houseNumber;
    private String description;

    @Override
    public String toString() {
        return super.toString(this);
    }
}
