package com.example.beautybook.repository.categoty;

import com.example.beautybook.model.Subcategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
    List<Subcategory> findAllByCategoryId(Long id);
}
