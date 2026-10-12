package com.laboratorio1.laboratorio1.mappers;

import com.laboratorio1.laboratorio1.dto.CategoryDTO;
import com.laboratorio1.laboratorio1.dto.CategorySummaryDto;
import com.laboratorio1.laboratorio1.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = ProductMapper.class)
public interface CategoryMapper {

    CategoryDTO toDto(Category category);
    CategorySummaryDto toSummaryDto(Category category);

    List<CategoryDTO> toDtoList(List<Category> categories);
    List<CategorySummaryDto> toSummaryDtoList(List<Category> categories);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productSet", ignore = true)
    Category toEntity(CategoryDTO categoryDTO);
}