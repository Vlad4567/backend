package com.example.beautybook.repository.mastercard;

import com.example.beautybook.model.MasterCard;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterCardRepository extends JpaRepository<MasterCard, Long>,
        JpaSpecificationExecutor<MasterCard> {
    Optional<MasterCard> findByUserUuid(String uuid);

    Optional<MasterCard> findMasterCardByIdAndIsHiddenIsFalse(Long id);

    @Query("FROM MasterCard m "
            + "WHERE m.isHidden = FALSE")
    Page<MasterCard> findAllByOrderByRatingDesc(Pageable pageable);

    @Modifying
    @Query("UPDATE MasterCard m SET m.rating = :rating WHERE m.id = :id")
    void updateRating(Long id, BigDecimal rating);

    boolean existsByUserEmail(String email);
}
