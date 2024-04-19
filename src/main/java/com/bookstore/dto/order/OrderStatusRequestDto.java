package com.bookstore.dto.order;

import jakarta.validation.constraints.NotBlank;

public record OrderStatusRequestDto(
        @NotBlank
        String status
) {
}
