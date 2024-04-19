package com.bookstore.service.impl;

import com.bookstore.dto.book.BookRequestDto;
import com.bookstore.dto.book.BookResponseDto;
import com.bookstore.dto.book.BookSearchParameters;
import com.bookstore.exception.EntityNotFoundException;
import com.bookstore.mapper.BookMapper;
import com.bookstore.model.Book;
import com.bookstore.model.Category;
import com.bookstore.repository.book.BookRepository;
import com.bookstore.repository.book.BookSpecificationBuilder;
import com.bookstore.repository.category.CategoryRepository;
import com.bookstore.service.BookService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private static final String BOOK_NOT_FOUND_ERROR = "Book doesn't exist. ID: ";
    private static final String CATEGORY_NOT_FOUND_ERROR = "Category doesn't exist. ID: ";

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    @Transactional
    public BookResponseDto save(BookRequestDto bookRequestDto) {
        Set<Category> categories = getCategoriesFromBookRequest(bookRequestDto);
        Book book = bookMapper.toEntity(bookRequestDto);
        book.setCategories(categories);
        Book savedBook = bookRepository.save(book);

        return bookMapper.toDto(savedBook);
    }

    @Override
    @Transactional
    public List<BookResponseDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public BookResponseDto findById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(BOOK_NOT_FOUND_ERROR + id));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional
    public BookResponseDto updateById(Long id, BookRequestDto bookRequestDto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(BOOK_NOT_FOUND_ERROR + id));

        Set<Category> categories = getCategoriesFromBookRequest(bookRequestDto);
        bookMapper.updateBookFromDto(existingBook, bookRequestDto);
        existingBook.setCategories(categories);
        Book updatedBook = bookRepository.save(existingBook);

        return bookMapper.toDto(updatedBook);
    }

    @Override
    @Transactional
    public List<BookResponseDto> search(BookSearchParameters params) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(params);

        return bookRepository.findAll(bookSpecification)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    private Set<Category> getCategoriesFromBookRequest(BookRequestDto bookRequestDto) {
        Set<Long> categoryIds = bookRequestDto.categoryIds();

        return categoryIds.stream()
                .map(c -> categoryRepository.findById(c)
                        .orElseThrow(() ->
                                new EntityNotFoundException(CATEGORY_NOT_FOUND_ERROR + c)))
                .collect(Collectors.toSet());
    }
}
