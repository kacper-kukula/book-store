package com.bookstore.repository.book.spec;

import com.bookstore.model.Book;
import com.bookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class MaxPriceSpecificationProvider implements SpecificationProvider<Book> {

    private static final String SPECIFICATION_KEY = "maxPrice";
    private static final int FIRST_INDEX = 0;

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("price"), params[FIRST_INDEX]);
    }

    @Override
    public String getKey() {
        return SPECIFICATION_KEY;
    }
}
