package com.laboratorio1.laboratorio1.controllers;

import com.laboratorio1.laboratorio1.dto.ProductDTO;
import com.laboratorio1.laboratorio1.mappers.ProductMapper;
import com.laboratorio1.laboratorio1.model.Product;
import com.laboratorio1.laboratorio1.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/product")
@Slf4j
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService, ProductMapper productMapper){
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER-ADMIN-ROLE', 'USER')")
    public List<ProductDTO> findAll(){
        log.info("Listando todos los productos");
        return productMapper.toDtoList(productService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER-ADMIN-ROLE', 'USER')")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id){
        log.info("Buscando producto con id {}", id);
        return ResponseEntity.of(productService.findById(id).map(productMapper::toDto));
    }

    @Secured("ROLE_SUPER-ADMIN-ROLE")
    @PostMapping("/")
    public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO productDTO) {
        log.info("Creando producto");
        Optional<Product> created = productService.create(
                productMapper.toEntity(productDTO), productDTO.category().id()
        );

        return created.map(product -> {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(product.getId())
                    .toUri();
            return ResponseEntity.created(location).body(productMapper.toDto(product));
        }).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Secured("ROLE_SUPER-ADMIN-ROLE")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(
            @PathVariable Long id,
            @RequestBody ProductDTO productDTO)
    {
        log.info("Actualizando producto con id {}", id);
        return ResponseEntity.of(
                productService.update(
                        id, productMapper.toEntity(productDTO), productDTO.category().id()
                ).map(productMapper::toDto)
        );
    }

    @Secured("ROLE_SUPER-ADMIN-ROLE")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Eliminando producto con id {}", id);
        return productService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}