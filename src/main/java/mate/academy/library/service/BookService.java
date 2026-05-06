package mate.academy.library.service;

import java.util.List;
import mate.academy.library.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
