package com.bookstore.mapper;

import com.bookstore.config.MapperConfig;
import com.bookstore.dto.book.BookRequestDto;
import com.bookstore.dto.book.BookResponseDto;
import com.bookstore.dto.book.BookResponseDtoWithoutCategoryIds;
import com.bookstore.model.Book;
import com.bookstore.model.Category;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    @Mapping(target = "categoryIds", source = "categories")
    BookResponseDto toDto(Book book);

    Book toEntity(BookRequestDto bookRequestDto);

    @Mapping(target = "id", ignore = true)
    void updateBookFromDto(@MappingTarget Book book, BookRequestDto dto);

    BookResponseDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    default Set<Long> mapCategoryToIds(Set<Category> categories) {
        return categories.stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
    }

    @Named("bookFromId")
    default Book bookFromId(Long id) {
        return null;
    }
}
