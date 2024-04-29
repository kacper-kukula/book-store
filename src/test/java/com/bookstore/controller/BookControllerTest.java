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

import com.bookstore.dto.book.BookRequestDto;
import com.bookstore.dto.book.BookResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {

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
                    new ClassPathResource("database/books/delete-all-books-and-categories.sql")
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
                    new ClassPathResource("database/books/add-5-books-with-categories.sql")
            );
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create a book with valid category ID")
    void createBook_ValidRequestDto_ReturnsCreatedBook() throws Exception {
        // Given
        BookRequestDto bookRequestDto = new BookRequestDto(
                "6th Book", "Author", "Isbn", BigDecimal.TEN,
                "description", "image", Set.of(1L));

        BookResponseDto expectedBookResponseDto = new BookResponseDto(
                6L, "6th Book", "Author", "Isbn", BigDecimal.TEN,
                "description", "image", Set.of(1L));

        String jsonRequest = objectMapper.writeValueAsString(bookRequestDto);

        // When
        MvcResult result = mockMvc.perform(
                        post("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        BookResponseDto actualBookResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookResponseDto.class);

        assertNotNull(actualBookResponseDto.id());
        EqualsBuilder.reflectionEquals(actualBookResponseDto, expectedBookResponseDto, "id");
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Find all books")
    void findAll_ReturnsAllBooks() throws Exception {
        // Given
        List<BookResponseDto> expected = new ArrayList<>();
        expected.add(new BookResponseDto(1L, "Harry Potter", "some author", "1",
                BigDecimal.valueOf(4.99), "description", "image", Set.of(1L, 3L)));
        expected.add(new BookResponseDto(2L, "Odyssey", "some author", "2",
                BigDecimal.valueOf(10.99), "description", "image", Set.of(2L)));
        expected.add(new BookResponseDto(3L, "LOTR", "some author", "3",
                BigDecimal.valueOf(14.99), "description", "image", Set.of(1L, 3L)));
        expected.add(new BookResponseDto(4L, "Hobbit", "some author", "4",
                BigDecimal.valueOf(49.99), "description", "image", Set.of(1L, 3L)));
        expected.add(new BookResponseDto(5L, "Da Vinci Code", "some author", "5",
                BigDecimal.valueOf(249.99), "description", "image", Set.of(4L)));

        // When
        MvcResult result = mockMvc.perform(
                        get("/books")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookResponseDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), BookResponseDto[].class);
        assertEquals(5, actual.length);
        assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Find correct book by ID")
    void findById_ExistingId_ReturnsCorrectBook() throws Exception {
        // Given
        Long existingId = 2L;
        BookResponseDto expected = new BookResponseDto(existingId, "Odyssey", "some author", "2",
                BigDecimal.valueOf(10.99), "description", "image", Set.of(2L));

        // When
        MvcResult result = mockMvc.perform(get("/books/" + existingId))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookResponseDto.class);
        assertNotNull(actual.id());
        EqualsBuilder.reflectionEquals(actual, expected, "id");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Update a book with valid book ID")
    void updateBook_ValidRequestDto_ReturnsUpdatedBook() throws Exception {
        // Given
        Long bookId = 1L;
        BookRequestDto bookRequestDto = new BookRequestDto(
                "Updated Title", "Updated Author", "Updated Isbn", BigDecimal.valueOf(20),
                "Updated description", "Updated image", Set.of(1L, 2L));

        BookResponseDto expectedBookResponseDto = new BookResponseDto(
                bookId, "Updated Title", "Updated Author", "Updated Isbn", BigDecimal.valueOf(20),
                "Updated description", "Updated image", Set.of(1L, 2L));

        String jsonRequest = objectMapper.writeValueAsString(bookRequestDto);

        // When
        MvcResult result = mockMvc.perform(
                        put("/books/" + bookId)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookResponseDto actualBookResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookResponseDto.class);

        assertNotNull(actualBookResponseDto.id());
        EqualsBuilder.reflectionEquals(expectedBookResponseDto, actualBookResponseDto);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Delete a book with valid book ID")
    void deleteBook_ValidBookId_ReturnsNoContent() throws Exception {
        // Given
        long bookId = 1L;
        boolean actualIsDeleted;

        // When
        mockMvc.perform(delete("/books/" + bookId))
                .andExpect(status().isOk())
                .andReturn();

        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement("SELECT * FROM books WHERE id = ?")) {
            statement.setLong(1, bookId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            actualIsDeleted = resultSet.getBoolean("is_deleted");
        }

        // Then
        assertTrue(actualIsDeleted);
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Search books by criteria")
    void searchBooks_ByCriteria_ReturnsMatchingBooks() throws Exception {
        // Given
        BigDecimal minPrice = BigDecimal.valueOf(2);
        BigDecimal maxPrice = BigDecimal.valueOf(6);

        List<BookResponseDto> expected = List.of(
                new BookResponseDto(1L, "Harry Potter", "some author", "1",
                        BigDecimal.valueOf(4.99), "description", "image", Set.of(1L, 3L)));

        // When
        MvcResult result = mockMvc.perform(
                        get("/books/search")
                                .param("minPrice", String.valueOf(minPrice))
                                .param("maxPrice", String.valueOf(maxPrice))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookResponseDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), BookResponseDto[].class);
        assertEquals(1, actual.length);
        assertEquals(expected, Arrays.stream(actual).toList());
    }
}
