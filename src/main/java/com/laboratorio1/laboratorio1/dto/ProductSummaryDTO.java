package com.laboratorio1.laboratorio1.dto;

import java.math.BigDecimal;

public record ProductSummaryDTO (
        Long id,
        String name,
        String description,
        BigDecimal price,
        int stock
) {
}