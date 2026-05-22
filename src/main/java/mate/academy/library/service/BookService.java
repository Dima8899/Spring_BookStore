package mate.academy.library.service;

import java.util.List;
import mate.academy.library.dto.BookDto;
import mate.academy.library.dto.CreateBookRequestDto;

public interface BookService {
    BookDto createBook(CreateBookRequestDto bookDto);

    List<BookDto> findAll();

    BookDto findBookById(Long id);

    BookDto updateBook(Long id, CreateBookRequestDto requestDto);

    void deleteBook(Long id);
}
