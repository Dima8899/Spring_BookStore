package mate.academy.library.dao;

import java.util.List;
import mate.academy.library.model.Book;

public interface BookRepository {

    Book save(Book book);

    List<Book> findAll();
}
