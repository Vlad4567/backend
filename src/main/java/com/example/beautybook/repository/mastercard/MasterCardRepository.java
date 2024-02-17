package com.example.beautybook.repository.mastercard;

import com.example.beautybook.model.MasterCard;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterCardRepository extends JpaRepository<MasterCard, Long>,
        JpaSpecificationExecutor<MasterCard> {
    @EntityGraph(attributePaths = {"gallery", "reviews","subcategories", "mainPhoto",
            "address", "location"})
    Optional<MasterCard> findByUserEmail(String email);

    @EntityGraph(attributePaths = {"gallery", "reviews", "subcategories",
            "mainPhoto", "address", "location", "serviceCards"})
    Optional<MasterCard> findMasterCardById(Long id);

    @EntityGraph(attributePaths = {"gallery", "reviews", "mainPhoto"})
    Page<MasterCard> findAllByOrderByRatingDesc(Pageable pageable);
}
