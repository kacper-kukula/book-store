package com.bookstore.dto.order;

import com.bookstore.model.Order;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequestDto(
        @NotNull(message = "status can't be empty.")
        Order.Status status
) {
}
