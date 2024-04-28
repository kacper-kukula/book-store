package com.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bookstore.dto.book.BookResponseDtoWithoutCategoryIds;
import com.bookstore.dto.category.CategoryRequestDto;
import com.bookstore.dto.category.CategoryResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {

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
                            "database/books/delete-all-books-and-categories.sql")
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
                            "database/category/add-4-categories-to-categories-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/category/add-2-books-with-categories-to-tables.sql")
            );
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create a category")
    void createCategory_ValidRequestDto_ReturnsCreatedCategory() throws Exception {
        // Given
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("Fiction", "description");

        // When
        MvcResult result = mockMvc.perform(
                        post("/categories")
                                .content(objectMapper.writeValueAsString(categoryRequestDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        CategoryResponseDto responseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryResponseDto.class);
        assertNotNull(responseDto.id());
        EqualsBuilder.reflectionEquals(categoryRequestDto, responseDto, "id");
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Find all categories")
    void findAll_ReturnsAllCategories() throws Exception {
        // Given
        List<CategoryResponseDto> expected = List.of(
                new CategoryResponseDto(1L, "Fantasy",
                        "Books that feature magic, mythical creatures, and imaginary worlds."),
                new CategoryResponseDto(2L, "Epic Poetry",
                        "Narratives that tell grand stories often involving heroes and gods."),
                new CategoryResponseDto(3L, "Adventure",
                        "Books filled with thrilling journeys and exciting escapades."),
                new CategoryResponseDto(4L, "Mystery",
                        "Stories that involve solving puzzles or uncovering secrets.")
        );

        // When
        MvcResult result = mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CategoryResponseDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), CategoryResponseDto[].class);
        assertEquals(expected.size(), actual.length);
        assertEquals(expected, Arrays.asList(actual));
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Find category by ID")
    void findById_ExistingId_ReturnsCategory() throws Exception {
        // Given
        Long categoryId = 1L;
        CategoryResponseDto expected = new CategoryResponseDto(categoryId, "Fantasy",
                "Books that feature magic, mythical creatures, and imaginary worlds.");

        // When
        MvcResult result = mockMvc.perform(get("/categories/" + categoryId))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CategoryResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryResponseDto.class);
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Update a category")
    void updateCategory_ValidRequestDto_ReturnsUpdatedCategory() throws Exception {
        // Given
        long categoryId = 2L;
        CategoryRequestDto updatedCategoryDto = new CategoryRequestDto("Fiction", "description");

        // When
        MvcResult result = mockMvc.perform(
                        put("/categories/" + categoryId)
                                .content(objectMapper.writeValueAsString(updatedCategoryDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CategoryResponseDto responseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryResponseDto.class);
        assertEquals(responseDto.id(), categoryId);
        EqualsBuilder.reflectionEquals(updatedCategoryDto, responseDto, "id");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Delete a category with valid category ID")
    void deleteCategory_ValidCategoryId_ReturnsNoContent() throws Exception {
        // Given
        long categoryId = 1L;
        boolean actualIsDeleted;

        // When
        mockMvc.perform(delete("/categories/" + categoryId))
                .andExpect(status().isOk())
                .andReturn();

        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement("SELECT * FROM categories WHERE id = ?")) {
            statement.setLong(1, categoryId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            actualIsDeleted = resultSet.getBoolean("is_deleted");
        }

        // Then
        assertTrue(actualIsDeleted);
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Find books by category ID")
    void findBooksByCategoryId_ExistingId_ReturnsBooks() throws Exception {
        // Given
        long categoryId = 1L;
        List<BookResponseDtoWithoutCategoryIds> expected = List.of(
                new BookResponseDtoWithoutCategoryIds(1L, "Harry Potter", "some author", "1",
                        BigDecimal.valueOf(4.99), "description", "image"),
                new BookResponseDtoWithoutCategoryIds(2L, "Odyssey", "some author", "2",
                        BigDecimal.valueOf(10.99), "description", "image")
        );

        // When
        MvcResult result = mockMvc.perform(get("/categories/" + categoryId + "/books"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookResponseDtoWithoutCategoryIds[] actual =
                objectMapper.readValue(result.getResponse().getContentAsByteArray(),
                        BookResponseDtoWithoutCategoryIds[].class);

        assertEquals(2, actual.length);
        assertEquals(expected, Arrays.stream(actual).toList());
    }
}
