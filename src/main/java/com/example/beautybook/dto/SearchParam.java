package com.example.beautybook.dto;

public record SearchParam(
        String[] text,
        String[] city,
        String[] category,
        String[] price,
        String[] sort
) {
}
