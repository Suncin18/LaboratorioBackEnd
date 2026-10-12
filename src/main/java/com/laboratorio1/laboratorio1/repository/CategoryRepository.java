package com.laboratorio1.laboratorio1.repository;

import com.laboratorio1.laboratorio1.model.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Override
    @EntityGraph(attributePaths = "productSet")
    List<Category> findAll();

    @Override
    @EntityGraph(attributePaths = "productSet")
    Optional<Category> findById(Long id);
}
