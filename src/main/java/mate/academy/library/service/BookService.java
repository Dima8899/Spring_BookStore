package mate.academy.library.service;

import java.util.List;
import mate.academy.library.dto.BookDto;
import mate.academy.library.dto.CreateBookRequestDto;
import mate.academy.library.dto.UpdateBookRequestDto;

public interface BookService {
    BookDto createBook(CreateBookRequestDto bookDto);

    List<BookDto> findAll();

    BookDto findBookById(Long id);

    BookDto updateBook(Long id, UpdateBookRequestDto requestDto);
}
