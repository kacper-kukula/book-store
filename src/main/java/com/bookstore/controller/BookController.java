package com.bookstore.controller;

import com.bookstore.dto.BookDto;
import com.bookstore.dto.CreateBookRequestDto;
import java.util.List;

public interface BookController {

    List<BookDto> getAll();

    BookDto getByBookId(Long id);

    BookDto createBook(CreateBookRequestDto createBookRequestDto);
}
