package com.example.beautybook.repository.photo;

import com.example.beautybook.model.Photo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findAllByMasterCardIdAndSubcategoryId(Long masterCardId, Long subcategoryId);
}
