package com.bookstore.controller;

import com.bookstore.dto.shoppingcart.CartItemRequestDto;
import com.bookstore.dto.shoppingcart.CartItemResponseDto;
import com.bookstore.dto.shoppingcart.ShoppingCartRequestDto;
import com.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User's Shopping Cart",
        description = "Endpoints for managing a shopping cart")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/cart")
public class ShoppingCartController {

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    @Operation(summary = "Retrieve Shopping Cart", description = "Retrieves current shopping cart"
            + " belonging to particular user")
    public ShoppingCartResponseDto findCart() {
        return null;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping
    @Operation(summary = "Add items to shopping cart", description = "Adds books to shopping "
            + "cart belonging to particular user")
    public ShoppingCartResponseDto addToCart(
            @RequestBody @Valid ShoppingCartRequestDto shoppingCartRequestDto) {
        return null;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/cart-items/{id}")
    @Operation(summary = "Update shopping cart", description = "Updates quantity of particular "
            + "book in the shopping cart")
    public CartItemResponseDto updateCartItem(
            @PathVariable Long id,
            @RequestBody @Valid CartItemRequestDto cartItemRequestDto) {
        return null;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/cart-items/{id}")
    @Operation(summary = "Delete item from shopping cart", description = "Deletes a particular "
            + "book from the shopping cart")
    public CartItemResponseDto deleteCartItem(@PathVariable Long id) {
        return null;
    }
}
