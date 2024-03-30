package com.bookstore.service;

import com.bookstore.dto.book.BookRequestDto;
import com.bookstore.dto.book.BookResponseDto;
import com.bookstore.dto.book.BookSearchParameters;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {

    BookResponseDto save(BookRequestDto bookRequestDto);

    List<BookResponseDto> findAll(Pageable pageable);

    BookResponseDto findById(Long id);

    void deleteById(Long id);

    BookResponseDto updateById(Long id, BookRequestDto bookRequestDto);

    List<BookResponseDto> search(BookSearchParameters params);
}
