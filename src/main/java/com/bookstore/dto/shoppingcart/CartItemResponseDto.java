package com.bookstore.dto.shoppingcart;

public record CartItemResponseDto(
        Long id,
        Long bookId,
        String bookTitle,
        Integer quantity
) {
}
