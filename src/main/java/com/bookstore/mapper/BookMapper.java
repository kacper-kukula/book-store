package com.bookstore.mapper;

import com.bookstore.config.MapperConfig;
import com.bookstore.dto.book.BookRequestDto;
import com.bookstore.dto.book.BookResponseDto;
import com.bookstore.dto.book.BookResponseDtoWithoutCategoryIds;
import com.bookstore.model.Book;
import com.bookstore.model.Category;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookResponseDto toDto(Book book);

    @Mapping(target = "categories", source = "categoriesSet")
    Book toModel(BookRequestDto bookRequestDto, Set<Category> categoriesSet);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", source = "categoriesSet")
    void updateBookFromDto(@MappingTarget Book book, BookRequestDto dto,
                           Set<Category> categoriesSet);

    BookResponseDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    default Set<String> mapCategoryToString(Set<Category> categories) {
        return categories.stream()
                .map(Category::getName)
                .collect(Collectors.toSet());
    }

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookResponseDto bookResponseDto, Book book) {

    }
}
