package com.laboratorio1.laboratorio1.service;

import com.laboratorio1.laboratorio1.model.Category;
import com.laboratorio1.laboratorio1.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll(){
        log.info("Buscando todas las categorias");
        return categoryRepository.findAll();
    }

    public Optional<Category> findById(Long id){
        log.info("Buscando categoria por id: {}", id);
        return categoryRepository.findById(id);
    }

    public Category create(Category category) {
        category.setId(null);
        category.getProductSet().clear();
        return categoryRepository.save(category);
    }

    @Transactional
    public Optional<Category> update(Long id, Category data) {
        return categoryRepository.findById(id).map(category -> {
            category.setName(data.getName());
            category.setDescription(data.getDescription());
            return categoryRepository.save(category);
        });
    }

    @Transactional
    public boolean delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            return false;
        }
        categoryRepository.deleteById(id);
        return true;
    }
}