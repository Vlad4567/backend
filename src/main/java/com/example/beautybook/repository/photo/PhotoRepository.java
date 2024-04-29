package com.example.beautybook.repository.photo;

import com.example.beautybook.model.Photo;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Page<Photo> findAllByMasterCardIdAndSubcategoryId(
            Pageable pageable, Long masterCardId, Long subcategoryId);

    Set<Photo> findPhotosByMasterCardId(Long id);
}
