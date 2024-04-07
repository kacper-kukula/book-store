package com.bookstore.repository.book;

import com.bookstore.dto.book.BookSearchParameters;
import com.bookstore.model.Book;
import com.bookstore.repository.SpecificationBuilder;
import com.bookstore.repository.SpecificationProviderManager;
import java.math.BigDecimal;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {

    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> spec = Specification.where(null);

        if (searchParameters.titles() != null && searchParameters.titles().length > 0) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider("title")
                            .getSpecification(searchParameters.titles()));
        }

        if (searchParameters.authors() != null && searchParameters.authors().length > 0) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider("author")
                    .getSpecification(searchParameters.authors()));
        }

        if (searchParameters.isbns() != null && searchParameters.isbns().length > 0) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider("isbn")
                    .getSpecification(searchParameters.isbns()));
        }

        if (searchParameters.descriptions() != null
                && searchParameters.descriptions().length > 0) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider("description")
                    .getSpecification(searchParameters.descriptions()));
        }

        if (searchParameters.minPrice() != null) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider("minPrice")
                    .getSpecification(
                            Stream.of(searchParameters.minPrice())
                            .map(BigDecimal::toString)
                            .toArray(String[]::new)));
        }

        if (searchParameters.maxPrice() != null) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider("maxPrice")
                    .getSpecification(
                            Stream.of(searchParameters.maxPrice())
                            .map(BigDecimal::toString)
                            .toArray(String[]::new)));
        }

        if (searchParameters.categories() != null && searchParameters.categories().length > 0) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider("categories")
                    .getSpecification(searchParameters.categories()));
        }

        return spec;
    }
}
