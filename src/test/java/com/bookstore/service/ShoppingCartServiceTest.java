package com.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bookstore.dto.shoppingcart.CartItemRequestDto;
import com.bookstore.dto.shoppingcart.CartItemResponseDto;
import com.bookstore.dto.shoppingcart.ShoppingCartRequestDto;
import com.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.bookstore.mapper.ShoppingCartMapper;
import com.bookstore.model.Book;
import com.bookstore.model.CartItem;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.User;
import com.bookstore.repository.book.BookRepository;
import com.bookstore.repository.shoppingcart.ShoppingCartRepository;
import com.bookstore.service.impl.ShoppingCartServiceImpl;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @BeforeEach
    void setUp() {
        User user = new User();

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);

        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("Test findCart() method for valid user")
    void findCart_ExistentUser_ReturnsShoppingCart() {
        // Given
        User user = new User();
        ShoppingCart shoppingCart = new ShoppingCart();
        ShoppingCartResponseDto expectedResponseDto =
                new ShoppingCartResponseDto(1L, 1L, Collections.emptySet());

        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.of(shoppingCart));
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(expectedResponseDto);

        // When
        ShoppingCartResponseDto actualResponseDto = shoppingCartService.findCart();

        // Then
        assertThat(actualResponseDto).isEqualTo(expectedResponseDto);
        verify(shoppingCartRepository, times(1)).findByUser(user);
        verify(shoppingCartMapper, times(1)).toDto(shoppingCart);
    }

    @Test
    @DisplayName("Test addToCart() method with valid bookId")
    void addToCart_ValidBookId_ReturnsUpdatedCart() {
        // Given
        final User user = new User();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setCartItems(new HashSet<>());

        final Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);

        final ShoppingCartRequestDto requestDto = new ShoppingCartRequestDto(bookId, 1);
        ShoppingCart savedShoppingCart = new ShoppingCart();
        savedShoppingCart.setCartItems(new HashSet<>());

        ShoppingCartResponseDto expectedResponseDto =
                new ShoppingCartResponseDto(1L, 1L, new HashSet<>());

        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.of(shoppingCart));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(shoppingCartRepository.save(shoppingCart)).thenReturn(savedShoppingCart);
        when(shoppingCartMapper.toDto(savedShoppingCart)).thenReturn(expectedResponseDto);

        // When
        ShoppingCartResponseDto actualResponseDto = shoppingCartService.addToCart(requestDto);

        // Then
        assertThat(actualResponseDto).isEqualTo(expectedResponseDto);
        verify(shoppingCartRepository, times(1)).findByUser(user);
        verify(bookRepository, times(1)).findById(bookId);
        verify(shoppingCartRepository, times(1)).save(shoppingCart);
        verify(shoppingCartMapper, times(1)).toDto(savedShoppingCart);
    }

    @Test
    @DisplayName("Test updateCartItem() method with valid cart item id")
    void updateCartItem_ValidItemId_ReturnsUpdatedCart() {
        // Given
        User user = new User();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);

        Long cartItemId = 1L;
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setQuantity(1);
        shoppingCart.setCartItems(Set.of(cartItem));

        CartItem updatedCartItem = new CartItem();
        updatedCartItem.setId(cartItemId);
        updatedCartItem.setQuantity(2);

        ShoppingCart savedShoppingCart = new ShoppingCart();
        savedShoppingCart.setUser(user);
        savedShoppingCart.setCartItems(Set.of(updatedCartItem));

        CartItemRequestDto requestDto = new CartItemRequestDto(2);
        CartItemResponseDto responseDto = new CartItemResponseDto(1L, 1L, "title", 2);

        ShoppingCartResponseDto expectedResponseDto =
                new ShoppingCartResponseDto(1L, 1L, Set.of(responseDto));

        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.of(shoppingCart));
        when(shoppingCartRepository.save(shoppingCart)).thenReturn(savedShoppingCart);
        when(shoppingCartMapper.toDto(savedShoppingCart)).thenReturn(expectedResponseDto);

        // When
        ShoppingCartResponseDto actualResponseDto =
                shoppingCartService.updateCartItem(cartItemId, requestDto);

        // Then
        assertThat(actualResponseDto).isEqualTo(expectedResponseDto);
        verify(shoppingCartRepository, times(1)).findByUser(user);
        verify(shoppingCartRepository, times(1)).save(shoppingCart);
        verify(shoppingCartMapper, times(1)).toDto(savedShoppingCart);
    }

    @Test
    @DisplayName("Test delete() method with valid cart item id")
    void delete_ValidItemId_ReturnsUpdatedCart() {
        // Given
        User user = new User();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);

        Long cartItemId = 1L;
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);

        Set<CartItem> cartItems = new HashSet<>();
        cartItems.add(cartItem);

        shoppingCart.setCartItems(cartItems);

        ShoppingCart savedShoppingCart = new ShoppingCart();
        savedShoppingCart.setUser(user);
        savedShoppingCart.setCartItems(Collections.emptySet());

        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.of(shoppingCart));
        when(shoppingCartRepository.save(shoppingCart)).thenReturn(savedShoppingCart);

        // When
        shoppingCartService.delete(cartItemId);

        // Then
        assertThat(savedShoppingCart).isEqualTo(shoppingCart);
        verify(shoppingCartRepository, times(1)).findByUser(user);
        verify(shoppingCartRepository, times(1)).save(shoppingCart);
    }
}
