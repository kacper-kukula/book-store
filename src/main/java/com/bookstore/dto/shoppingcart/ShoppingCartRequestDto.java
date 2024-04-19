package com.bookstore.dto.shoppingcart;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ShoppingCartRequestDto(
        @NotNull
        @Positive
        Long bookId,

        @NotNull
        @Min(value = 0)
        @Max(value = 20)
        Integer quantity
) {
}
