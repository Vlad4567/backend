package com.example.beautybook.repository.servicecard;

import com.example.beautybook.model.ServiceCard;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceCardRepository extends JpaRepository<ServiceCard, Long>,
        JpaSpecificationExecutor<ServiceCard> {
    @EntityGraph(attributePaths = "subcategory")
    List<ServiceCard> findAllByMasterCardId(Long masterCardId);

    @NotNull
    @EntityGraph(attributePaths = {
            "subcategory",
            "masterCard",
            "masterCard.address",
            "masterCard.subcategories"
    })
    Page<ServiceCard> findAll(@NotNull Specification<ServiceCard> spec, @NotNull Pageable pageable);
}
