package com.example.beautybook.repository;

import com.example.beautybook.model.MasterCard;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterCardRepository extends JpaRepository<MasterCard, Long> {
    @EntityGraph(attributePaths = "gallery")
    Optional<MasterCard> findByUserEmail(String email);
}
