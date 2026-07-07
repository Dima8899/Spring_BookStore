package mate.academy.library.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import mate.academy.library.model.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("save() persists category and it can be found by id")
    void save_validCategory_savesAndReturnsCategory() {
        Category category = new Category();
        category.setName("Fantasy");
        category.setDescription("Fantasy books");

        Category saved = categoryRepository.save(category);

        assertThat(saved.getId()).isNotNull();
        Optional<Category> found = categoryRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Fantasy");
    }

    @Test
    @DisplayName("deleteById() soft-deletes: not findable, but row still exists in DB")
    void delete_existingCategory_softDeletedNotReturnedByFindById() {
        Category category = new Category();
        category.setName("Sci-Fi");
        Category saved = entityManager.persistFlushFind(category);
        Long id = saved.getId();

        categoryRepository.deleteById(id);
        entityManager.flush();
        entityManager.clear();

        Optional<Category> found = categoryRepository.findById(id);
        assertThat(found).isEmpty();

        Object[] row = (Object[]) entityManager.getEntityManager()
                .createNativeQuery("SELECT id, CAST(is_deleted AS BOOLEAN) FROM categories WHERE id = ?")
                .setParameter(1, id)
                .getSingleResult();
        assertThat(row[1]).isEqualTo(true);
    }
}
