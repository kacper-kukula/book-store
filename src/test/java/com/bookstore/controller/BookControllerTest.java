package com.bookstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bookstore.dto.book.BookRequestDto;
import com.bookstore.dto.book.BookResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public class BookControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createBook_ValidRequestDto_Success() throws Exception {
        BookRequestDto bookRequestDto = new BookRequestDto(
                "Title", "Author", "Isbn", BigDecimal.TEN,
                "description", "image", Set.of(1L));

        BookResponseDto expectedBookResponseDto = new BookResponseDto(
                1L, "Title", "Author", "Isbn", BigDecimal.TEN,
                "description", "image", Set.of(1L));

        String jsonRequest = objectMapper.writeValueAsString(bookRequestDto);

        MvcResult result = mockMvc.perform(
                        post("api/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
    }
}
