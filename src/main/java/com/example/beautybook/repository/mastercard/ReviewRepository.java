package com.example.beautybook.repository.mastercard;

import com.example.beautybook.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @EntityGraph(attributePaths = "masterCard")
    Review save(Review review);

    @EntityGraph(attributePaths = {"masterCard", "user"})
    Page<Review> findAllByMasterCardId(Long id, Pageable pageable);

    @Query("SELECT AVG(r.grade) FROM Review r WHERE r.masterCard.id = :masterId")
    Double getAverageRatingByMasterId(@Param("masterId") Long masterId);
}
