package com.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.bookstore.model.ShoppingCart;
import com.bookstore.model.User;
import com.bookstore.repository.shoppingcart.ShoppingCartRepository;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartRepositoryTest {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @BeforeAll
    static void beforeAll(@Autowired DataSource dataSource) throws SQLException {
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

    @Test
    @DisplayName("Find Shopping Cart for current user")
    void findByUser_ValidUserId_ReturnsShoppingCart() {
        User user = new User();
        user.setId(1L);

        ShoppingCart actual = shoppingCartRepository.findByUser(user)
                .orElseGet(() -> {
                    fail("Shopping Cart is expected, but was not found.");
                    return null;
                });

        assertEquals(1, actual.getCartItems().size());
        assertEquals(1, actual.getUser().getId());
    }

    @Test
    @DisplayName("Find Shopping Cart for invalid user")
    void findByUser_NonExistentUserId_ReturnsEmptyOptional() {
        User user = new User();
        user.setId(1234L);

        Optional<ShoppingCart> actual = shoppingCartRepository.findByUser(user);

        assertTrue(actual.isEmpty());
    }
}
