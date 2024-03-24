package com.bookstore.dto;

import java.math.BigDecimal;

public record BookSearchParameters(
        String[] titles,
        String[] authors,
        String[] isbns,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        String[] descriptions
) {}
