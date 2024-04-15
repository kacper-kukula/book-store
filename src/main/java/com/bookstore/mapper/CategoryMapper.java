package com.bookstore.mapper;

import com.bookstore.config.MapperConfig;
import com.bookstore.dto.category.CategoryRequestDto;
import com.bookstore.dto.category.CategoryResponseDto;
import com.bookstore.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {

    CategoryResponseDto toDto(Category category);

    Category toEntity(CategoryRequestDto categoryRequestDto);

    @Mapping(target = "id", ignore = true)
    void updateCategoryFromDto(@MappingTarget Category category, CategoryRequestDto dto);
}
