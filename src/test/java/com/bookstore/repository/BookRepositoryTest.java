package com.bookstore.repository;

import com.bookstore.model.Book;
import com.bookstore.repository.book.BookRepository;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
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
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @BeforeAll
    static void beforeAll(@Autowired DataSource dataSource) throws SQLException {
        teardown(dataSource);

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/add-5-books-with-categories.sql")
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
                    new ClassPathResource("database/books/delete-all-books-and-categories.sql")
            );
        }
    }

    @Test
    @DisplayName("""
            Find all books belonging to Adventure category
            """)
    void findAllByCategoriesId_CategoryId3_ReturnsThreeBooks() {
        Long adventureCategoryId = 3L;

        List<Book> actual = bookRepository.findAllByCategoriesId(adventureCategoryId);

        Assertions.assertEquals(3, actual.size());
        Assertions.assertEquals("Harry Potter", actual.get(0).getTitle());
        Assertions.assertEquals("LOTR", actual.get(1).getTitle());
        Assertions.assertEquals("Hobbit", actual.get(2).getTitle());
    }

    @Test
    @DisplayName("""
            Find all books when no books belong to a Cooking category
            """)
    void findAllByCategoriesId_NoBooksInCategory_ReturnsEmptyList() {
        Long cookingCategoryId = 5L;

        List<Book> actual = bookRepository.findAllByCategoriesId(cookingCategoryId);

        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("""
            Find all books when category ID does not exist
            """)
    void findAllByCategoriesId_NonExistingCategory_ReturnsEmptyList() {
        Long nonExistentCategoryId = 999L;

        List<Book> actual = bookRepository.findAllByCategoriesId(nonExistentCategoryId);

        Assertions.assertTrue(actual.isEmpty());
    }
}
