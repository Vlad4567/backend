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

    @Query(value = "SELECT * FROM master_card m WHERE m.is_hidden = FALSE", nativeQuery = true)
    Page<MasterCard> findAllByOrderByRatingDesc(Pageable pageable);

    @Modifying
    @Query(value = "UPDATE master_card SET rating = :rating WHERE id = :id", nativeQuery = true)
    void updateRating(Long id, BigDecimal rating);

    @Query(value = "SELECT * FROM user_favorite_cards u JOIN u.card_id where u.user_id = :userId", nativeQuery = true)
    Page<MasterCard> findMasterCardFavoriteByUserId(Long userId, Pageable pageable);

    boolean existsByUserEmail(String email);
}
