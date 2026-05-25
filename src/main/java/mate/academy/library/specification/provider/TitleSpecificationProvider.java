package mate.academy.library.specification.provider;

import mate.academy.library.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    private static final String TITLE = "title";

    @Override
    public Specification<Book> getSpecification(String param) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get(TITLE)),
                        "%" + param.toLowerCase() + "%");
    }
}
