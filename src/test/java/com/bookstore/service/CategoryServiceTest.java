package com.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.bookstore.dto.book.BookResponseDtoWithoutCategoryIds;
import com.bookstore.dto.category.CategoryRequestDto;
import com.bookstore.dto.category.CategoryResponseDto;
import com.bookstore.mapper.BookMapper;
import com.bookstore.mapper.CategoryMapper;
import com.bookstore.model.Book;
import com.bookstore.model.Category;
import com.bookstore.repository.book.BookRepository;
import com.bookstore.repository.category.CategoryRepository;
import com.bookstore.service.impl.CategoryServiceImpl;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("""
            Verify that findAll() method works
            """)
    void findAll_ValidPageable_ReturnsAllCategories() {
        // Given
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Fiction");
        category1.setDescription("Description");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Non-Fiction");
        category2.setDescription("Description");

        CategoryResponseDto categoryDto1 = new CategoryResponseDto(
                1L, "Fiction", "Description");

        CategoryResponseDto categoryDto2 = new CategoryResponseDto(
                2L, "Non-Fiction", "Description");

        List<Category> categories = List.of(category1, category2);
        Page<Category> categoryPage =
                new PageImpl<>(categories, Pageable.unpaged(), categories.size());

        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(categoryPage);
        when(categoryMapper.toDto(category1)).thenReturn(categoryDto1);
        when(categoryMapper.toDto(category2)).thenReturn(categoryDto2);

        // When
        List<CategoryResponseDto> actual = categoryService.findAll(Pageable.unpaged());

        // Then
        assertThat(actual).hasSize(2);
        assertThat(actual.get(0)).isEqualTo(categoryDto1);
        assertThat(actual.get(1)).isEqualTo(categoryDto2);
        verify(categoryRepository, times(1)).findAll(any(Pageable.class));
        verify(categoryMapper, times(1)).toDto(category1);
        verify(categoryMapper, times(1)).toDto(category2);
    }

    @Test
    @DisplayName("""
            Verify that findById() method returns correct category by ID
            """)
    void findById_ValidId_ReturnsCorrectCategory() {
        // Given
        Category category = new Category();
        category.setId(1L);
        category.setName("Fiction");
        category.setDescription("Description");

        CategoryResponseDto expectedCategoryResponseDto = new CategoryResponseDto(
                1L, "Fiction", "Description");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expectedCategoryResponseDto);

        // When
        CategoryResponseDto actualCategoryResponseDto = categoryService.findById(1L);

        // Then
        assertThat(actualCategoryResponseDto).isEqualTo(expectedCategoryResponseDto);
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryMapper, times(1)).toDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("""
            Verify that save() method works with valid category
            """)
    void save_WithValidCategoryRequestDto_ReturnsValidCategoryResponseDto() {
        // Given
        final CategoryRequestDto categoryRequestDto = new CategoryRequestDto(
                "Horror", "Description");

        Category category = new Category();
        category.setId(1L);
        category.setName("Horror");
        category.setDescription("Description");

        CategoryResponseDto expectedResponseDto = new CategoryResponseDto(
                1L, "Horror", "Description");

        when(categoryMapper.toEntity(categoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expectedResponseDto);

        // When
        CategoryResponseDto savedResponseDto = categoryService.save(categoryRequestDto);

        // Then
        assertThat(savedResponseDto).isEqualTo(expectedResponseDto);
        verify(categoryMapper, times(1)).toEntity(categoryRequestDto);
        verify(categoryRepository, times(1)).save(category);
        verify(categoryMapper, times(1)).toDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("""
            Verify that updateById() method updates category name and description
            """)
    void updateById_ValidId_ReturnsUpdatedCategoryResponseDto() {
        // Given
        final CategoryRequestDto categoryRequestDto = new CategoryRequestDto(
                "New Horror", "New Description");

        Category existingCategory = new Category();
        existingCategory.setId(1L);
        existingCategory.setName("Horror");
        existingCategory.setDescription("Description");

        Category updatedCategory = new Category();
        updatedCategory.setId(1L);
        updatedCategory.setName("New Horror");
        updatedCategory.setDescription("New Description");

        CategoryResponseDto expectedResponseDto = new CategoryResponseDto(
                1L, "New Horror", "New Description");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(existingCategory)).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(expectedResponseDto);

        // When
        CategoryResponseDto actualResponseDto = categoryService.updateById(1L, categoryRequestDto);

        // Then
        assertThat(actualResponseDto).isEqualTo(expectedResponseDto);
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(existingCategory);
        verify(categoryMapper, times(1)).toDto(updatedCategory);
        verify(categoryMapper, times(1))
                .updateCategoryFromDto(existingCategory, categoryRequestDto);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("""
            Verify that deleteById() method works
            """)
    void deleteById_ValidId_CategoryDeletedSuccessfully() {
        // Given
        Long categoryId = 1L;

        // When
        categoryService.deleteById(categoryId);

        // Then
        verify(categoryRepository, times(1)).deleteById(categoryId);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("""
            Verify that findBooksByCategory() method returns books
             belonging to the specified category id
            """)
    void findBooksByCategory_ValidCategoryId_ReturnsBooksBelongingToCategory() {
        // Given
        final Long categoryId = 1L;

        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Title 1");
        book1.setAuthor("Author 1");
        book1.setIsbn("ISBN 1");
        book1.setPrice(BigDecimal.valueOf(10.99));
        book1.setDescription("Description 1");
        book1.setCoverImage("Image 1");
        book1.setCategories(Collections.emptySet());

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Title 2");
        book2.setAuthor("Author 2");
        book2.setIsbn("ISBN 2");
        book2.setPrice(BigDecimal.valueOf(20.99));
        book2.setDescription("Description 2");
        book2.setCoverImage("Image 2");
        book2.setCategories(Collections.emptySet());

        BookResponseDtoWithoutCategoryIds bookResponseDto1 = new BookResponseDtoWithoutCategoryIds(
                1L, "Title 1", "Author 1", "ISBN 1", BigDecimal.valueOf(10.99),
                "Description 1", "Image 1");

        BookResponseDtoWithoutCategoryIds bookResponseDto2 = new BookResponseDtoWithoutCategoryIds(
                2L, "Title 2", "Author 2", "ISBN 2", BigDecimal.valueOf(20.99),
                "Description 2", "Image 2");

        List<Book> books = List.of(book1, book2);
        List<BookResponseDtoWithoutCategoryIds> expectedResponseDtos =
                List.of(bookResponseDto1, bookResponseDto2);

        when(bookRepository.findAllByCategoriesId(categoryId)).thenReturn(books);
        when(bookMapper.toDtoWithoutCategories(book1)).thenReturn(bookResponseDto1);
        when(bookMapper.toDtoWithoutCategories(book2)).thenReturn(bookResponseDto2);

        // When
        List<BookResponseDtoWithoutCategoryIds> actualResponseDtos =
                categoryService.findBooksByCategory(categoryId);

        // Then
        assertThat(actualResponseDtos).containsExactlyInAnyOrderElementsOf(expectedResponseDtos);
        verify(bookRepository, times(1)).findAllByCategoriesId(categoryId);
        verify(bookMapper, times(1)).toDtoWithoutCategories(book1);
        verify(bookMapper, times(1)).toDtoWithoutCategories(book2);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }
}
