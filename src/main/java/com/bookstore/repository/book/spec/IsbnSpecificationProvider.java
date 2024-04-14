package com.bookstore.repository.book.spec;

import com.bookstore.model.Book;
import com.bookstore.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class IsbnSpecificationProvider implements SpecificationProvider<Book> {

    private static final String SPECIFICATION_KEY = "isbn";

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                root.get(SPECIFICATION_KEY).in(Arrays.stream(params).toArray());
    }

    @Override
    public String getKey() {
        return SPECIFICATION_KEY;
    }
}
