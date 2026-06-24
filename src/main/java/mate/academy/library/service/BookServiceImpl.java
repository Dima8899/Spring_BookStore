package mate.academy.library.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.library.dao.BookRepository;
import mate.academy.library.dto.BookDto;
import mate.academy.library.dto.BookDtoWithoutCategoryIds;
import mate.academy.library.dto.BookSearchParametersDto;
import mate.academy.library.dto.CreateBookRequestDto;
import mate.academy.library.exception.EntityNotFoundException;
import mate.academy.library.mapper.BookMapper;
import mate.academy.library.model.Book;
import mate.academy.library.specification.BookSpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder specificationBuilder;

    @Override
    public BookDto createBook(CreateBookRequestDto bookRequestDto) {
        Book book = bookMapper.toModel(bookRequestDto);

        Book savedBook = bookRepository.save(book);

        return bookMapper.toDto(savedBook);
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public BookDto findBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find book by id"
                ));
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto updateBook(Long id, CreateBookRequestDto requestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id " + id));
        bookMapper.updateBookFromDto(requestDto, book);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Page<BookDto> searchBooks(BookSearchParametersDto searchParameters, Pageable pageable) {
        Specification<Book> spec = specificationBuilder.build(searchParameters);
        return bookRepository.findAll(spec, pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public List<BookDtoWithoutCategoryIds> getBooksByCategoriesId(Long categoryId) {
        return bookRepository.findAllByCategoriesId(categoryId).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }
}
