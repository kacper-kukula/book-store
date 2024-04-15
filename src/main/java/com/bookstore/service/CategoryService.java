package com.bookstore.service;

import com.bookstore.dto.book.BookResponseDtoWithoutCategoryIds;
import com.bookstore.dto.category.CategoryRequestDto;
import com.bookstore.dto.category.CategoryResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    List<CategoryResponseDto> findAll(Pageable pageable);

    CategoryResponseDto findById(Long id);

    CategoryResponseDto save(CategoryRequestDto categoryRequestDto);

    CategoryResponseDto updateById(Long id, CategoryRequestDto categoryRequestDto);

    void deleteById(Long id);

    List<BookResponseDtoWithoutCategoryIds> findBooksByCategory(Long id);
}
