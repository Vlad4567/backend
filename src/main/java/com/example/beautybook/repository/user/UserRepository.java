package com.example.beautybook.repository.user;

import com.example.beautybook.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    Optional<User> findByUuid(String uuid);

    @Query("SELECT u.email FROM User u")
    List<String> findAllEmail();

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    @Query("SELECT u.refreshToken FROM User u WHERE u.email = :email")
    String getRefreshTokenByEmail(String email);

    Optional<User> findByTelegramAccountChatId(Long chatId);
}
