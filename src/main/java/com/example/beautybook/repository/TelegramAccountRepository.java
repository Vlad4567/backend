package com.example.beautybook.repository;

import com.example.beautybook.model.TelegramAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramAccountRepository extends JpaRepository<TelegramAccount, Long> {
    boolean existsByChatId(Long chatId);
}
