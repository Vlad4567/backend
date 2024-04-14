package com.example.beautybook.dto.address;

import com.example.beautybook.model.City;
import lombok.Data;

@Data
public class AddressWithCityDto {
    private City city;
    private String street;
    private String houseNumber;
    private String description;
}
