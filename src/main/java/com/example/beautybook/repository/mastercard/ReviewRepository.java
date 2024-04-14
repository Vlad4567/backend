package com.example.beautybook.repository.mastercard;

import com.example.beautybook.model.Review;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByMasterCardId(Long id, Pageable pageable);

    @Query("SELECT AVG(r.grade) FROM Review r WHERE r.masterCard.id = :masterId")
    Double getAverageRatingByMasterId(@Param("masterId") Long masterId);

    @Query("SELECT "
            + "SUM(CASE WHEN r.grade = 1 THEN 1 ELSE 0 END) AS count1, "
            + "SUM(CASE WHEN r.grade = 2 THEN 1 ELSE 0 END) AS count2, "
            + "SUM(CASE WHEN r.grade = 3 THEN 1 ELSE 0 END) AS count3, "
            + "SUM(CASE WHEN r.grade = 4 THEN 1 ELSE 0 END) AS count4, "
            + "SUM(CASE WHEN r.grade = 5 THEN 1 ELSE 0 END) AS count5 "
            + "FROM Review r "
            + "LEFT JOIN r.masterCard m "
            + "WHERE m.id = :id")
    Map<String, Long> getCountsByGrade(Long id);
}
