package com.laboratorio1.laboratorio1.service;

import com.laboratorio1.laboratorio1.model.Product;
import com.laboratorio1.laboratorio1.repository.CategoryRepository;
import com.laboratorio1.laboratorio1.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository
    ){
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Product> findAll(){
        log.info("Buscando todos los productos");
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id){
        log.info("Buscando producto por id: {}", id);
        return productRepository.findById(id);
    }

    public Optional<Product> create(Product product, Long categoryId) {
        product.setId(null);
        return categoryRepository.findById(categoryId).map(category -> {
            product.setCategory(category);
            return productRepository.save(product);
        });
    }

    @Transactional
    public Optional<Product> update(Long id, Product data, Long categoryId) {
        return productRepository.findById(id).flatMap(product ->
                categoryRepository.findById(categoryId).map(category -> {
                product.setName(data.getName());
                product.setDescription(data.getDescription());
                product.setPrice(data.getPrice());
                product.setStock(data.getStock());
                product.setCategory(category);
                return productRepository.save(product);
            })
        );
    }

    @Transactional
    public boolean delete(Long id) {
        if (!productRepository.existsById(id)) {
            return false;
        }
        productRepository.deleteById(id);
        return true;
    }
}
