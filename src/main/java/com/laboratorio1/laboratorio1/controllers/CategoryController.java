package com.laboratorio1.laboratorio1.controllers;

import com.laboratorio1.laboratorio1.dto.CategoryDTO;
import com.laboratorio1.laboratorio1.mappers.CategoryMapper;
import com.laboratorio1.laboratorio1.model.Category;
import com.laboratorio1.laboratorio1.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper){
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER-ADMIN-ROLE', 'USER')")
    public List<CategoryDTO> findAll(){
        log.info("Buscando todas las categorias");
        return categoryMapper.toDtoList(categoryService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER-ADMIN-ROLE', 'USER')")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id){
        log.info("Buscando la categoria con el id {}", id);
        return ResponseEntity.of(categoryService.findById(id).map(categoryMapper::toDto));
    }

    @Secured("ROLE_SUPER-ADMIN-ROLE")
    @PostMapping("/")
    public ResponseEntity<CategoryDTO> create(@RequestBody CategoryDTO categoryDTO) {
        log.info("Creando categoria");
        Category created = categoryService.create(categoryMapper.toEntity(categoryDTO));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(categoryMapper.toDto(created));
    }

    @Secured("ROLE_SUPER-ADMIN-ROLE")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> update(
            @PathVariable Long id,
            @RequestBody CategoryDTO categoryDTO)
    {
        log.info("Actualizando categoria con id {}", id);
        return ResponseEntity.of(categoryService.update(
                id, categoryMapper.toEntity(categoryDTO)).map(categoryMapper::toDto)
        );
    }

    @Secured("ROLE_SUPER-ADMIN-ROLE")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Eliminando categoria con id {}", id);
        return categoryService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}