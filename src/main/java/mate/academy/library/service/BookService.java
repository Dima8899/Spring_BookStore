package mate.academy.library.service;

import java.util.List;
import mate.academy.library.dto.BookDto;
import mate.academy.library.dto.BookSearchParametersDto;
import mate.academy.library.dto.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto createBook(CreateBookRequestDto bookDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findBookById(Long id);

    BookDto updateBook(Long id, CreateBookRequestDto requestDto);

    void deleteBook(Long id);

    List<BookDto> searchBooks(BookSearchParametersDto searchParameters, Pageable pageable);
}
