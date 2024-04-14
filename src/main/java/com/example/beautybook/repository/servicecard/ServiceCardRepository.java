package com.example.beautybook.repository.servicecard;

import com.example.beautybook.model.ServiceCard;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceCardRepository extends JpaRepository<ServiceCard, Long>,
        JpaSpecificationExecutor<ServiceCard> {
    List<ServiceCard> findAllByMasterCardId(Long masterCardId);

    List<ServiceCard> findAllByMasterCardIdAndSubcategoryId(Long masterCardId, Long subcategoryId);

    List<ServiceCard> findAllByPhotoId(Long id);
}
