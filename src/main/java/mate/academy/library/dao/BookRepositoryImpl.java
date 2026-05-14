package mate.academy.library.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import mate.academy.library.model.Book;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryImpl implements BookRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Book save(Book book) {
        entityManager.persist(book);
        return book;
    }

    @Override
    public List<Book> findAll() {
        return entityManager
                .createQuery("FROM Book", Book.class)
                .getResultList();
    }
}
