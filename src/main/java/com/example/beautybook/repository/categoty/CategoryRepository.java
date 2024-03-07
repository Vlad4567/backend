package com.example.beautybook.repository.categoty;

import com.example.beautybook.model.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @EntityGraph(attributePaths = "subcategories")
    List<Category> findAll();

    @EntityGraph(attributePaths = "subcategories")
    Optional<Category> findById(Long id);
}
