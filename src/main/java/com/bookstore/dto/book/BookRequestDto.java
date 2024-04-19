package com.bookstore.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;

public record BookRequestDto(
        @NotBlank
        @Size(max = 100)
        String title,

        @NotBlank
        @Size(max = 50)
        String author,

        @NotBlank
        @Size(max = 13)
        String isbn,

        @NotNull
        @PositiveOrZero
        BigDecimal price,

        @NotBlank
        @Size(max = 255)
        String description,

        @NotBlank
        @Size(max = 255)
        String coverImage,

        @NotNull
        Set<Long> categoryIds
) {
}
