package mate.academy.library.specification;

import mate.academy.library.dto.BookSearchParametersDto;
import mate.academy.library.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class BookSpecificationBuilder {

    public Specification<Book> build(BookSearchParametersDto params) {
        Specification<Book> spec = Specification.where(null);

        if (params.getTitle() != null && !params.getTitle().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("title")),
                            "%" + params.getTitle().toLowerCase() + "%"));
        }

        if (params.getAuthor() != null && !params.getAuthor().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("author")),
                            "%" + params.getAuthor().toLowerCase() + "%"));
        }

        if (params.getIsbn() != null && !params.getIsbn().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("isbn"), params.getIsbn()));
        }

        if (params.getMinPrice() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("price"), params.getMinPrice()));
        }

        if (params.getMaxPrice() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("price"), params.getMaxPrice()));
        }

        return spec;
    }
}
