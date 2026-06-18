package mate.academy.library.service;

import java.util.List;
import mate.academy.library.dto.BookDto;
import mate.academy.library.dto.BookDtoWithoutCategoryIds;
import mate.academy.library.dto.BookSearchParametersDto;
import mate.academy.library.dto.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto createBook(CreateBookRequestDto bookDto);
    Page<BookDto> findAll(Pageable pageable);
    BookDto findBookById(Long id);
    BookDto updateBook(Long id, CreateBookRequestDto requestDto);
    void deleteBook(Long id);
    Page<BookDto> searchBooks(BookSearchParametersDto searchParameters, Pageable pageable);
    List<BookDtoWithoutCategoryIds> getBooksByCategoriesId(Long categoryId);
}
