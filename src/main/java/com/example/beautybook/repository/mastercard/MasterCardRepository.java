package com.example.beautybook.repository.mastercard;

import com.example.beautybook.model.MasterCard;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterCardRepository extends JpaRepository<MasterCard, Long>,
        JpaSpecificationExecutor<MasterCard> {
    @EntityGraph(attributePaths = {"gallery","subcategories", "mainPhoto",
            "address", "address.city", "location", "contacts"})
    Optional<MasterCard> findByUserEmail(String email);

    @EntityGraph(attributePaths = {"gallery", "subcategories", "contacts",
            "mainPhoto", "address", "address.city", "location"})
    Optional<MasterCard> findMasterCardByIdAndIsHiddenFalse(Long id);

    @Query("FROM MasterCard m "
            + "LEFT JOIN m.address a "
            + "LEFT JOIN m.mainPhoto mp "
            + "WHERE m.isHidden = FALSE")
    Page<MasterCard> findAllByOrderByRatingDesc(Pageable pageable);

    @Modifying
    @Query("UPDATE MasterCard m SET m.rating = :rating WHERE m.id = :id")
    void updateRating(Long id, BigDecimal rating);

    @Query("SELECT "
            + "SUM(CASE WHEN r.grade = 1 THEN 1 ELSE 0 END) AS count1, "
            + "SUM(CASE WHEN r.grade = 2 THEN 1 ELSE 0 END) AS count2, "
            + "SUM(CASE WHEN r.grade = 3 THEN 1 ELSE 0 END) AS count3, "
            + "SUM(CASE WHEN r.grade = 4 THEN 1 ELSE 0 END) AS count4, "
            + "SUM(CASE WHEN r.grade = 5 THEN 1 ELSE 0 END) AS count5 "
            + "FROM MasterCard m " + "LEFT JOIN m.reviews r "
            + "WHERE m.id = :id")
    Map<String, Long> getCountsByGrade(Long id);

    boolean existsByUserEmail(String email);
}
