package com.bookstore.repository.book;

import com.bookstore.model.Book;
import com.bookstore.repository.SpecificationProvider;
import com.bookstore.repository.SpecificationProviderManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {

    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {

        return bookSpecificationProviders.stream()
                .filter(sp -> sp.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Can't find correct specification "
                        + "provider for key " + key));
    }
}
