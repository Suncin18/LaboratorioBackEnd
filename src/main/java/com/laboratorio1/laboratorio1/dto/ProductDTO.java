package com.laboratorio1.laboratorio1.dto;

import java.math.BigDecimal;

public record ProductDTO(
        Long id,
        String name,
        String description,
        BigDecimal price,
        int stock,
        CategorySummaryDto category
) {
}