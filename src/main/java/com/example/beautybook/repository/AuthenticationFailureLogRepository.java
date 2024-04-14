package com.example.beautybook.repository;

import com.example.beautybook.model.AuthenticationFailureLog;
import java.time.LocalDateTime;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationFailureLogRepository
        extends JpaRepository<AuthenticationFailureLog, Long> {
    Optional<AuthenticationFailureLog> findByToken(String token);

    void deleteById(@NotNull Long id);

    @Query("SELECT COUNT(a) "
            + "FROM AuthenticationFailureLog a "
            + "WHERE a.date >= :startTime AND a.ip = :ip")
    int countLogsAddedSinceByIp(LocalDateTime startTime, String ip);
}
