package com.bookstore.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequestDto(
        @NotBlank
        @Size(max = 100)
        String name,

        @NotBlank
        @Size(max = 255)
        String description
) {}
