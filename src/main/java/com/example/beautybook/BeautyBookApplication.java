package com.example.beautybook;

import com.example.beautybook.model.MasterCard;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BeautyBookApplication {
    public static void main(String[] args) {
        SpringApplication.run(BeautyBookApplication.class, args);
        MasterCard masterCard = new MasterCard(1L);
        System.out.println(masterCard.toString());
    }
}

