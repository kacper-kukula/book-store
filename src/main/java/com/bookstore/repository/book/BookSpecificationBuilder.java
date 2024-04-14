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

    private static final int ZERO_LENGTH = 0;
    private static final String TITLE_SPECIFICATION_NAME = "title";
    private static final String AUTHOR_SPECIFICATION_NAME = "author";
    private static final String ISBN_SPECIFICATION_NAME = "isbn";
    private static final String DESCRIPTION_SPECIFICATION_NAME = "description";
    private static final String MIN_PRICE_SPECIFICATION_NAME = "minPrice";
    private static final String MAX_PRICE_SPECIFICATION_NAME = "maxPrice";
    private static final String CATEGORY_IDS_SPECIFICATION_NAME = "categoryIds";

    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> spec = Specification.where(null);

        if (searchParameters.titles() != null && searchParameters.titles().length > ZERO_LENGTH) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider(TITLE_SPECIFICATION_NAME)
                    .getSpecification(searchParameters.titles()));
        }

        if (searchParameters.authors() != null
                && searchParameters.authors().length > ZERO_LENGTH) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider(AUTHOR_SPECIFICATION_NAME)
                    .getSpecification(searchParameters.authors()));
        }

        if (searchParameters.isbns() != null && searchParameters.isbns().length > ZERO_LENGTH) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider(ISBN_SPECIFICATION_NAME)
                    .getSpecification(searchParameters.isbns()));
        }

        if (searchParameters.descriptions() != null
                && searchParameters.descriptions().length > ZERO_LENGTH) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider(DESCRIPTION_SPECIFICATION_NAME)
                    .getSpecification(searchParameters.descriptions()));
        }

        if (searchParameters.minPrice() != null) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider(MIN_PRICE_SPECIFICATION_NAME)
                    .getSpecification(
                            Stream.of(searchParameters.minPrice())
                            .map(BigDecimal::toString)
                            .toArray(String[]::new)));
        }

        if (searchParameters.maxPrice() != null) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider(MAX_PRICE_SPECIFICATION_NAME)
                    .getSpecification(
                            Stream.of(searchParameters.maxPrice())
                            .map(BigDecimal::toString)
                            .toArray(String[]::new)));
        }

        if (searchParameters.categories() != null
                && searchParameters.categories().length > ZERO_LENGTH) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider(CATEGORY_IDS_SPECIFICATION_NAME)
                    .getSpecification(searchParameters.categories()));
        }

        return spec;
    }
}
