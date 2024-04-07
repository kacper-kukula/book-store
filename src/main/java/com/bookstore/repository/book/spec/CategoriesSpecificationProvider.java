package com.bookstore.repository.book.spec;

import com.bookstore.model.Book;
import com.bookstore.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CategoriesSpecificationProvider implements SpecificationProvider<Book> {

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                root.get("categories").in(Arrays.stream(params).toArray());
    }

    @Override
    public String getKey() {
        return "categories";
    }
}
