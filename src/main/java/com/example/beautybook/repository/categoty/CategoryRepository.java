package com.example.beautybook.repository.categoty;

import com.example.beautybook.model.Category;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    //@Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.subcategories")
    //List<Category> findAllCategoriesWithSubcategories();
}
