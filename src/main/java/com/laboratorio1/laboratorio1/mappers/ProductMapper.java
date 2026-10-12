package com.laboratorio1.laboratorio1.mappers;

import com.laboratorio1.laboratorio1.dto.ProductDTO;
import com.laboratorio1.laboratorio1.dto.ProductSummaryDTO;
import com.laboratorio1.laboratorio1.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
    ProductDTO toDto(Product product);
    ProductSummaryDTO toSummaryDto(Product product);

    List<ProductDTO> toDtoList(List<Product> products);
    List<ProductSummaryDTO> toSummaryDtoList(List<Product> products);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductDTO productDTO);
}