package com.bookstore.dto.book;

import java.math.BigDecimal;
import java.util.Set;

public record BookResponseDto(
        Long id,
        String title,
        String author,
        String isbn,
        BigDecimal price,
        String description,
        String coverImage,
        Set<Long> categoryIds
) {
}
