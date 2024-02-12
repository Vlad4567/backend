package com.example.beautybook.repository;

import com.example.beautybook.model.MasterCard;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterCardRepository extends JpaRepository<MasterCard, Long> {
    @EntityGraph(attributePaths = {"gallery", "reviews","subcategories", "mainPhoto"})
    Optional<MasterCard> findByUserEmail(String email);

    @EntityGraph(attributePaths = {"gallery", "reviews", "subcategories", "mainPhoto"})
    Optional<MasterCard> findMasterCardById(Long id);

    @EntityGraph(attributePaths = {"gallery", "reviews", "mainPhoto"})
    Page<MasterCard> findAllByOrderByRatingDesc(Pageable pageable);
}
