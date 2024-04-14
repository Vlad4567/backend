package com.example.beautybook.repository;

import com.example.beautybook.model.TelegramCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelegramCodeRepository extends JpaRepository<TelegramCode, Long> {
    boolean existsByCode(Long code);
}
