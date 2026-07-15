package mate.academy.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import mate.academy.library.dao.BookRepository;
import mate.academy.library.dto.BookDto;
import mate.academy.library.dto.BookDtoWithoutCategoryIds;
import mate.academy.library.dto.BookSearchParametersDto;
import mate.academy.library.dto.CreateBookRequestDto;
import mate.academy.library.exception.EntityNotFoundException;
import mate.academy.library.mapper.BookMapper;
import mate.academy.library.model.Book;
import mate.academy.library.specification.BookSpecificationBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookSpecificationBuilder specificationBuilder;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Book One");
        book.setAuthor("Author One");
        book.setIsbn("111-111");
        book.setPrice(BigDecimal.valueOf(19.99));

        bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Book One");
        bookDto.setAuthor("Author One");
        bookDto.setIsbn("111-111");
        bookDto.setPrice(BigDecimal.valueOf(19.99));
    }

    @Test
    @DisplayName("createBook() valid request returns saved BookDto")
    void createBook_validRequest_returnsBookDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Book One");
        requestDto.setAuthor("Author One");
        requestDto.setIsbn("111-111");
        requestDto.setPrice(BigDecimal.valueOf(19.99));

        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.createBook(requestDto);

        assertThat(result.getTitle()).isEqualTo("Book One");
        verify(bookRepository).save(book);
    }

    @Test
    @DisplayName("findAll() returns page of BookDto")
    void findAll_booksExist_returnsPageOfBookDto() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        Page<BookDto> result = bookService.findAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Book One");
    }

    @Test
    @DisplayName("findBookById() existing id returns BookDto")
    void findBookById_existingId_returnsBookDto() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.findBookById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("findBookById() non-existing id throws EntityNotFoundException")
    void findBookById_nonExistingId_throwsEntityNotFoundException() {
        when(bookRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.findBookById(100L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Can't find book by id");
    }

    @Test
    @DisplayName("updateBook() existing id updates and returns BookDto")
    void updateBook_existingId_returnsUpdatedBookDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Updated Title");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.updateBook(1L, requestDto);

        assertThat(result).isNotNull();
        verify(bookMapper, times(1)).updateBookFromDto(requestDto, book);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    @DisplayName("updateBook() non-existing id throws EntityNotFoundException")
    void updateBook_nonExistingId_throwsEntityNotFoundException() {
        when(bookRepository.findById(100L)).thenReturn(Optional.empty());

        CreateBookRequestDto requestDto = new CreateBookRequestDto();

        assertThatThrownBy(() -> bookService.updateBook(100L, requestDto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(bookRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteBook() delegates to repository")
    void deleteBook_validId_callsRepositoryDeleteById() {
        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("searchBooks() builds specification and returns page of BookDto")
    void searchBooks_validParameters_returnsPageOfBookDto() {
        BookSearchParametersDto searchParams = new BookSearchParametersDto();
        searchParams.setAuthor("Author One");

        Pageable pageable = PageRequest.of(0, 10);
        Specification<Book> spec = Specification.where(null);
        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);

        when(specificationBuilder.build(searchParams)).thenReturn(spec);
        when(bookRepository.findAll(spec, pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        Page<BookDto> result = bookService.searchBooks(searchParams, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getAuthor()).isEqualTo("Author One");
    }

    @Test
    @DisplayName("getBooksByCategoriesId() returns list of BookDtoWithoutCategoryIds")
    void getBooksByCategoriesId_existingCategory_returnsListOfBooks() {
        BookDtoWithoutCategoryIds shortDto = new BookDtoWithoutCategoryIds();
        shortDto.setId(1L);
        shortDto.setTitle("Book One");

        when(bookRepository.findAllByCategoriesId(5L)).thenReturn(List.of(book));
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(shortDto);

        List<BookDtoWithoutCategoryIds> result = bookService.getBooksByCategoriesId(5L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Book One");
    }

    @Test
    @DisplayName("getBooksByCategoriesId() no books found returns empty list")
    void getBooksByCategoriesId_noBooks_returnsEmptyList() {
        when(bookRepository.findAllByCategoriesId(999L)).thenReturn(List.of());

        List<BookDtoWithoutCategoryIds> result = bookService.getBooksByCategoriesId(999L);

        assertThat(result).isEmpty();
    }
}
