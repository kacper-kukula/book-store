package com.bookstore.service.impl;

import com.bookstore.dto.shoppingcart.CartItemRequestDto;
import com.bookstore.dto.shoppingcart.ShoppingCartRequestDto;
import com.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.bookstore.exception.EntityNotFoundException;
import com.bookstore.mapper.ShoppingCartMapper;
import com.bookstore.model.Book;
import com.bookstore.model.CartItem;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.User;
import com.bookstore.repository.book.BookRepository;
import com.bookstore.repository.shoppingcart.ShoppingCartRepository;
import com.bookstore.service.ShoppingCartService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    @Transactional
    public ShoppingCartResponseDto findCart() {
        User user = getCurrentUser();
        ShoppingCart shoppingCart = getOrCreateCartByUser(user);

        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto addToCart(ShoppingCartRequestDto shoppingCartRequestDto) {
        User user = getCurrentUser();
        ShoppingCart shoppingCart = getOrCreateCartByUser(user);

        Long bookId = shoppingCartRequestDto.bookId();
        Book bookFromDb = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Book not found with id: " + bookId));

        CartItem newCartItem = new CartItem();
        newCartItem.setBook(bookFromDb);
        newCartItem.setShoppingCart(shoppingCart);
        newCartItem.setQuantity(shoppingCartRequestDto.quantity());

        shoppingCart.getCartItems().add(newCartItem);
        ShoppingCart savedShoppingCart = shoppingCartRepository.save(shoppingCart);

        return shoppingCartMapper.toDto(savedShoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto updateCartItem(Long id, CartItemRequestDto cartItemRequestDto) {
        User user = getCurrentUser();
        ShoppingCart shoppingCart = getOrCreateCartByUser(user);
        Integer quantityRequest = cartItemRequestDto.quantity();

        CartItem cartItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new EntityNotFoundException("Cart item not found with id: " + id));

        cartItem.setQuantity(quantityRequest);
        ShoppingCart savedShoppingCart = shoppingCartRepository.save(shoppingCart);

        return shoppingCartMapper.toDto(savedShoppingCart);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = getCurrentUser();
        ShoppingCart shoppingCart = getOrCreateCartByUser(user);

        shoppingCart.getCartItems()
                .removeIf(item -> item.getId().equals(id));

        shoppingCartRepository.save(shoppingCart);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    private ShoppingCart getOrCreateCartByUser(User user) {
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findByUser(user);

        if (shoppingCart.isEmpty()) {
            ShoppingCart newCart = new ShoppingCart();
            newCart.setUser(user);
            return shoppingCartRepository.save(newCart);
        }

        return shoppingCart.get();
    }
}
