package com.bookstore.service;

import com.bookstore.dto.shoppingcart.CartItemRequestDto;
import com.bookstore.dto.shoppingcart.ShoppingCartRequestDto;
import com.bookstore.dto.shoppingcart.ShoppingCartResponseDto;

public interface ShoppingCartService {

    ShoppingCartResponseDto findCart();

    ShoppingCartResponseDto addToCart(ShoppingCartRequestDto shoppingCartRequestDto);

    ShoppingCartResponseDto updateCartItem(Long id, CartItemRequestDto cartItemRequestDto);

    void delete(Long id);
}
