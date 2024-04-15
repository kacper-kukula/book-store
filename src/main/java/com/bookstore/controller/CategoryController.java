package com.bookstore.controller;

import com.bookstore.dto.book.BookResponseDtoWithoutCategoryIds;
import com.bookstore.dto.category.CategoryRequestDto;
import com.bookstore.dto.category.CategoryResponseDto;
import com.bookstore.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category controller", description = "Endpoints for managing books categoryIds")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create new category", description = "Creates a new book category")
    public CategoryResponseDto createCategory(
            @RequestBody @Valid CategoryRequestDto categoryRequestDto) {
        return categoryService.save(categoryRequestDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    @Operation(summary = "Find all categoryIds", description = "Find all available categoryIds")
    public List<CategoryResponseDto> findAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    @Operation(summary = "Find a category by ID", description = "Find a category if it exists")
    public CategoryResponseDto findById(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update a category", description = "Update a category by ID")
    public CategoryResponseDto updateById(
            @PathVariable Long id,
            @RequestBody @Valid CategoryRequestDto categoryRequestDto) {
        return categoryService.updateById(id, categoryRequestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category", description = "Soft delete a category by ID")
    public void deleteById(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}/books")
    @Operation(summary = "Find all books belonging to that category",
            description = "Find a list of books belonging to specific category")
    public List<BookResponseDtoWithoutCategoryIds> findBooksByCategoryId(@PathVariable Long id) {
        return categoryService.findBooksByCategory(id);
    }
}
