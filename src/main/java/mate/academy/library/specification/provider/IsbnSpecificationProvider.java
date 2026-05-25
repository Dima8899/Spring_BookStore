package mate.academy.library.specification.provider;

import mate.academy.library.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class IsbnSpecificationProvider implements SpecificationProvider<Book> {
    private static final String ISBN = "isbn";

    @Override
    public Specification<Book> getSpecification(String param) {
        return (root, query, cb) ->
                cb.equal(root.get(ISBN), param);
    }
}
