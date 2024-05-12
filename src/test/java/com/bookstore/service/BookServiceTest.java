package com.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.bookstore.dto.book.BookRequestDto;
import com.bookstore.dto.book.BookResponseDto;
import com.bookstore.dto.book.BookSearchParameters;
import com.bookstore.mapper.BookMapper;
import com.bookstore.model.Book;
import com.bookstore.model.Category;
import com.bookstore.repository.book.BookRepository;
import com.bookstore.repository.book.BookSpecificationBuilder;
import com.bookstore.repository.category.CategoryRepository;
import com.bookstore.service.impl.BookServiceImpl;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Verify that save() method works with valid book")
    void save_WithValidBookRequestDto_ReturnsValidBookResponseDto() {
        // Given
        final BookRequestDto bookRequestDto = new BookRequestDto(
                "Odyssey", "Some Author", "000000001", BigDecimal.TEN,
                "description", "image", Set.of(2L));

        final BookResponseDto bookResponseDto = new BookResponseDto(
                1L, "Odyssey", "Some Author", "000000001", BigDecimal.TEN,
                "description", "image", Set.of(2L));

        Category category = new Category();
        category.setId(2L);
        category.setName("Epic Poetry");
        category.setDescription("Description");

        Book book = new Book();
        book.setTitle("Odyssey");
        book.setAuthor("Some Author");
        book.setIsbn("000000001");
        book.setPrice(BigDecimal.TEN);
        book.setDescription("description");
        book.setCoverImage("image");
        book.setCategories(Set.of(category));

        when(bookMapper.toEntity(bookRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookResponseDto);
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));

        // When
        BookResponseDto savedBookResponseDto = bookService.save(bookRequestDto);

        //Then
        assertThat(savedBookResponseDto).isEqualTo(bookResponseDto);
        verify(bookRepository, times(1)).save(book);
        verify(categoryRepository, times(1)).findById(2L);
        verifyNoMoreInteractions(bookRepository, bookMapper, categoryRepository);
    }

    @Test
    @DisplayName("Verify that findAll() method works")
    void findAll_ValidPageable_ReturnsAllBooks() {
        // Given
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Title 1");
        book1.setAuthor("Author 1");
        book1.setIsbn("ISBN 1");
        book1.setPrice(BigDecimal.valueOf(10.99));
        book1.setDescription("Description 1");
        book1.setCoverImage("Image 1");
        book1.setCategories(Collections.emptySet());

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Title 2");
        book2.setAuthor("Author 2");
        book2.setIsbn("ISBN 2");
        book2.setPrice(BigDecimal.valueOf(20.99));
        book2.setDescription("Description 2");
        book2.setCoverImage("Image 2");
        book2.setCategories(Collections.emptySet());

        BookResponseDto bookResponseDto1 = new BookResponseDto(
                1L, "Title 1", "Author 1", "ISBN 1", BigDecimal.valueOf(10.99),
                "Description 1", "Image 1", Collections.emptySet());

        BookResponseDto bookResponseDto2 = new BookResponseDto(
                2L, "Title 2", "Author 2", "ISBN 2", BigDecimal.valueOf(20.99),
                "Description 2", "Image 2", Collections.emptySet());

        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(book1, book2);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());
        List<BookResponseDto> expectedBookResponseDtos =
                List.of(bookResponseDto1, bookResponseDto2);

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book1)).thenReturn(bookResponseDto1);
        when(bookMapper.toDto(book2)).thenReturn(bookResponseDto2);

        // When
        List<BookResponseDto> actualBookResponseDtos = bookService.findAll(pageable);

        // Then
        assertThat(actualBookResponseDtos).containsExactlyElementsOf(expectedBookResponseDtos);
        verify(bookRepository, times(1)).findAll(pageable);
        verify(bookMapper, times(1)).toDto(book1);
        verify(bookMapper, times(1)).toDto(book2);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify that findById() method returns correct book by ID")
    void findById_ValidId_ReturnsCorrectBook() {
        // Given
        Long testBookId = 1L;
        Book book = new Book();
        book.setId(testBookId);
        book.setTitle("Title 1");
        book.setAuthor("Author 1");
        book.setIsbn("ISBN 1");
        book.setPrice(BigDecimal.valueOf(10.99));
        book.setDescription("Description 1");
        book.setCoverImage("Image 1");
        book.setCategories(Collections.emptySet());

        BookResponseDto expectedBookResponseDto = new BookResponseDto(
                testBookId, "Title 1", "Author 1", "ISBN 1", BigDecimal.valueOf(10.99),
                "Description 1", "Image 1", Collections.emptySet());

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expectedBookResponseDto);

        // When
        BookResponseDto actualBookResponseDto = bookService.findById(testBookId);

        // Then
        assertThat(actualBookResponseDto).isEqualTo(expectedBookResponseDto);
        verify(bookRepository, times(1)).findById(testBookId);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify that deleteById() method works")
    void deleteById_ValidId_BookDeletedSuccessfully() {
        // Given
        Long bookId = 1L;

        // When
        bookService.deleteById(bookId);

        // Then
        verify(bookRepository, times(1)).deleteById(bookId);
        verifyNoMoreInteractions(bookRepository, bookMapper, categoryRepository);
    }

    @Test
    @DisplayName("Verify that updateById() method updates book ISBN and price")
    void updateById_ValidId_ReturnsUpdatedBookResponseDto() {
        // Given
        Long testBookId = 1L;

        final BookRequestDto bookRequestDto = new BookRequestDto(
                "Title", "Author", "000000001", BigDecimal.valueOf(15.99),
                "Description", "Image", Collections.emptySet());

        final BookResponseDto expectedBookResponseDto = new BookResponseDto(
                testBookId, "New Title", "Author", "000000002", BigDecimal.valueOf(15.99),
                "Description", "Image", Collections.emptySet());

        Book existingBook = new Book();
        existingBook.setId(testBookId);
        existingBook.setTitle("Title");
        existingBook.setAuthor("Author");
        existingBook.setIsbn("000000001");
        existingBook.setPrice(BigDecimal.valueOf(15.99));
        existingBook.setDescription("Description");
        existingBook.setCoverImage("Image");
        existingBook.setCategories(Collections.emptySet());

        final Book updatedBook = new Book();
        existingBook.setId(testBookId);
        existingBook.setTitle("New Title");
        existingBook.setAuthor("Author");
        existingBook.setIsbn("000000002");
        existingBook.setPrice(BigDecimal.valueOf(15.99));
        existingBook.setDescription("Description");
        existingBook.setCoverImage("Image");
        existingBook.setCategories(Collections.emptySet());

        when(bookRepository.findById(testBookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(updatedBook);
        when(bookMapper.toDto(updatedBook)).thenReturn(expectedBookResponseDto);

        // When
        BookResponseDto actualBookResponseDto = bookService.updateById(testBookId, bookRequestDto);

        // Then
        assertThat(actualBookResponseDto).isEqualTo(expectedBookResponseDto);
        verify(bookRepository, times(1)).findById(testBookId);
        verify(bookRepository, times(1)).save(existingBook);
        verify(bookMapper, times(1)).toDto(updatedBook);
        verify(bookMapper, times(1)).updateBookFromDto(existingBook, bookRequestDto);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify that search() method works")
    void search_ValidSearchParameters_ReturnsMatchingBooks() {
        // Given
        final BookSearchParameters params = new BookSearchParameters(
                new String[]{"Title 1", "Title 2"},
                new String[]{"Author 1", "Author 2"},
                new String[]{"ISBN 1", "ISBN 2"},
                BigDecimal.ZERO,
                BigDecimal.valueOf(50.0),
                new String[]{"Description 1", "Description 2"},
                new String[]{"Category 1"}
        );

        Category category = new Category();
        category.setId(1L);
        category.setName("Horror");
        category.setDescription("Description");

        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Title 1");
        book1.setAuthor("Author 1");
        book1.setIsbn("ISBN 1");
        book1.setPrice(BigDecimal.TEN);
        book1.setDescription("Description 1");
        book1.setCoverImage("Image 1");
        book1.setCategories(Set.of(category));

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Title 2");
        book2.setAuthor("Author 2");
        book2.setIsbn("ISBN 2");
        book2.setPrice(BigDecimal.valueOf(20.0));
        book2.setDescription("Description 2");
        book2.setCoverImage("Image 2");
        book2.setCategories(Set.of(category));

        BookResponseDto book1Dto = new BookResponseDto(
                1L, "Title 1", "Author 1", "ISBN 1", BigDecimal.TEN,
                "Description 1", "Image 1", Set.of(1L)
        );

        BookResponseDto book2Dto = new BookResponseDto(
                2L, "Title 2", "Author 2", "ISBN 2", BigDecimal.valueOf(20.0),
                "Description 2", "Image 2", Set.of(1L)
        );

        List<Book> matchingBooks = List.of(book1, book2);
        Specification<Book> bookSpecification = Mockito.mock(Specification.class);

        when(bookSpecificationBuilder.build(params)).thenReturn(bookSpecification);
        when(bookRepository.findAll(bookSpecification)).thenReturn(matchingBooks);
        when(bookMapper.toDto(book1)).thenReturn(book1Dto);
        when(bookMapper.toDto(book2)).thenReturn(book2Dto);

        // When
        List<BookResponseDto> actual = bookService.search(params);

        // Then
        assertEquals(2, actual.size());
        assertThat(actual.get(0)).isEqualTo(book1Dto);
        assertThat(actual.get(1)).isEqualTo(book2Dto);
        verify(bookMapper, times(1)).toDto(book1);
        verify(bookMapper, times(1)).toDto(book2);
        verify(bookSpecificationBuilder, times(1)).build(params);
        verify(bookRepository, times(1)).findAll(any(Specification.class));
    }
}
