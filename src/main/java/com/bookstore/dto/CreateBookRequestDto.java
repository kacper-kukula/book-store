package com.bookstore.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {

    @NotNull
    @Size(min = 1, max = 100)
    private String title;

    @NotNull
    @Size(min = 1, max = 50)
    private String author;

    @NotNull
    @Size(min = 10, max = 13)
    private String isbn;

    @NotNull
    @PositiveOrZero
    private BigDecimal price;

    @NotNull
    @Size(max = 255)
    private String description;

    @NotNull
    @Size(max = 255)
    private String coverImage;
}
