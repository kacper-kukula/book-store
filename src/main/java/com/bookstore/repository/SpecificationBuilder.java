package com.bookstore.repository;

import com.bookstore.dto.book.BookSearchParameters;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {

    Specification<T> build(BookSearchParameters searchParameters);
}
