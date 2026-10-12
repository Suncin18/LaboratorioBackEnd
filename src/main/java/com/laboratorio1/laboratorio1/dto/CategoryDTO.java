package com.laboratorio1.laboratorio1.dto;

import java.util.List;

public record CategoryDTO(
        Long id,
        String name,
        String description,
        List<ProductSummaryDTO> products
) {
}