package com.example.beautybook.repository.mastercard;

import com.example.beautybook.model.Review;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @EntityGraph(attributePaths = "masterCard")
    Review save(Review review);

    @EntityGraph(attributePaths = {"masterCard", "user"})
    List<Review> findAllByMasterCardId(Long id);
}
