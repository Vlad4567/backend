package com.example.beautybook.repository.user;

import com.example.beautybook.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(String email);

    @Query("SELECT email from User")
    List<String> findAllEmail();

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    @Query("SELECT u.refreshToken FROM User u WHERE u.email = :email")
    String getRefreshTokenByEmail(String email);
}
