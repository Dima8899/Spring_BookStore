package mate.academy.library.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.library.dao.BookRepository;
import mate.academy.library.model.Book;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}
