package com.bookstore.dto.shoppingcart;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemRequestDto(
        @NotNull
        @Min(value = 0)
        @Max(value = 20)
        Integer quantity
) {
}
