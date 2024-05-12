package com.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bookstore.dto.shoppingcart.CartItemRequestDto;
import com.bookstore.dto.shoppingcart.CartItemResponseDto;
import com.bookstore.dto.shoppingcart.ShoppingCartRequestDto;
import com.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private DataSource dataSource;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/shoppingcart/delete-all-shopping-carts-with-items.sql")
            );
        }
    }

    @BeforeEach
    void setup() throws SQLException {
        teardown(dataSource);

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/shoppingcart/add-shopping-cart-with-items.sql")
            );
        }
    }

    @Test
    @WithUserDetails("admin@book-store.com")
    @DisplayName("Retrieve Shopping Cart for logged in User")
    void findCart_ValidUser_ReturnsShoppingCart() throws Exception {
        // Given
        CartItemResponseDto cartItemResponseDto =
                new CartItemResponseDto(1L, 1L, "Harry Potter", 5);
        ShoppingCartResponseDto expected =
                new ShoppingCartResponseDto(1L, 1L, Set.of(cartItemResponseDto));

        // When
        MvcResult result = mockMvc.perform(
                        get("/cart"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        ShoppingCartResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartResponseDto.class);
        EqualsBuilder.reflectionEquals(actual, expected);
    }

    @Test
    @WithUserDetails("admin@book-store.com")
    @DisplayName("Add items to shopping cart")
    void addToCart_AddsItemsToShoppingCart_ReturnsShoppingCart() throws Exception {
        // Given
        ShoppingCartRequestDto requestDto = new ShoppingCartRequestDto(2L, 3);
        CartItemResponseDto cartItemExisting =
                new CartItemResponseDto(1L, 1L, "Harry Potter", 5);
        CartItemResponseDto cartItemAdded =
                new CartItemResponseDto(2L, 2L, "Odyssey", 3);
        ShoppingCartResponseDto expected =
                new ShoppingCartResponseDto(1L, 1L, Set.of(cartItemAdded, cartItemExisting));

        // When
        MvcResult result = mockMvc.perform(
                        post("/cart")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        ShoppingCartResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartResponseDto.class);
        EqualsBuilder.reflectionEquals(actual, expected);
    }

    @Test
    @WithUserDetails("admin@book-store.com")
    @DisplayName("Update shopping cart")
    void updateCartItem_UpdatesShoppingCart() throws Exception {
        // Given
        long cartItemId = 1L;
        CartItemRequestDto requestDto = new CartItemRequestDto(2);
        CartItemResponseDto cartItemResponseDto =
                new CartItemResponseDto(1L, 1L, "Harry Potter", 2);
        ShoppingCartResponseDto expected =
                new ShoppingCartResponseDto(1L, 1L, Set.of(cartItemResponseDto));

        // When
        MvcResult result = mockMvc.perform(
                        put("/cart/cart-items/" + cartItemId)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        ShoppingCartResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartResponseDto.class);
        EqualsBuilder.reflectionEquals(actual, expected);
    }

    @Test
    @WithUserDetails("admin@book-store.com")
    @DisplayName("Delete item from shopping cart")
    void deleteCartItem_DeletesItemFromShoppingCart() throws Exception {
        // Given
        long cartItemId = 1L;
        long shoppingCartId = 1L;

        // When
        mockMvc.perform(delete("/cart/cart-items/" + cartItemId))
                .andExpect(status().isOk());

        // Then
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM cart_items WHERE shopping_cart_id = ?")) {
            statement.setLong(1, shoppingCartId);
            ResultSet resultSet = statement.executeQuery();
            boolean cartItemExists = resultSet.next();

            assertFalse(cartItemExists);
        }
    }
}
