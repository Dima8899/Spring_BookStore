package mate.academy.library.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import mate.academy.library.model.Book;
import mate.academy.library.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Category fantasyCategory;
    private Category historyCategory;

    @BeforeEach
    void setUp() {
        fantasyCategory = new Category();
        fantasyCategory.setName("Fantasy");
        fantasyCategory = entityManager.persistFlushFind(fantasyCategory);

        historyCategory = new Category();
        historyCategory.setName("History");
        historyCategory = entityManager.persistFlushFind(historyCategory);
    }

    @Test
    @DisplayName("findAllByCategoriesId() returns only books linked to given category")
    void findAllByCategoriesId_existingCategory_returnsMatchingBooks() {
        entityManager.persistAndFlush(createBook("Book One", "111-111", fantasyCategory));
        entityManager.persistAndFlush(createBook("Book Two", "222-222", historyCategory));

        List<Book> result = bookRepository.findAllByCategoriesId(fantasyCategory.getId());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Book One");
    }

    @Test
    @DisplayName("findAllByCategoriesId() returns empty list when category has no books")
    void findAllByCategoriesId_noBooksInCategory_returnsEmptyList() {
        List<Book> result = bookRepository.findAllByCategoriesId(historyCategory.getId());

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findAllByCategoriesId() excludes soft-deleted books")
    void findAllByCategoriesId_softDeletedBook_excludedFromResult() {
        Book saved = entityManager.persistFlushFind(
                createBook("Deleted Book", "333-333", fantasyCategory));

        bookRepository.deleteById(saved.getId());
        entityManager.flush();
        entityManager.clear();

        List<Book> result = bookRepository.findAllByCategoriesId(fantasyCategory.getId());

        assertThat(result).isEmpty();
    }

    private Book createBook(String title, String isbn, Category category) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor("Some Author");
        book.setIsbn(isbn);
        book.setPrice(BigDecimal.valueOf(19.99));
        book.setCategories(new HashSet<>(Set.of(category)));
        return book;
    }
}
