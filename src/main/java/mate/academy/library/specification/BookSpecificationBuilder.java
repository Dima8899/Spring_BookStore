package mate.academy.library.specification;

import lombok.RequiredArgsConstructor;
import mate.academy.library.dto.BookSearchParametersDto;
import mate.academy.library.model.Book;
import mate.academy.library.specification.provider.AuthorSpecificationProvider;
import mate.academy.library.specification.provider.IsbnSpecificationProvider;
import mate.academy.library.specification.provider.MaxPriceSpecificationProvider;
import mate.academy.library.specification.provider.MinPriceSpecificatorProvider;
import mate.academy.library.specification.provider.TitleSpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder {
    private final TitleSpecificationProvider titleSpecificationProvider;
    private final AuthorSpecificationProvider authorSpecificationProvider;
    private final IsbnSpecificationProvider isbnSpecificationProvider;
    private final MinPriceSpecificatorProvider minPriceSpecificatorProvider;
    private final MaxPriceSpecificationProvider maxPriceSpecificationProvider;

    public Specification<Book> build(BookSearchParametersDto params) {
        Specification<Book> spec = Specification.where(null);

        if (params.getTitle() != null && !params.getTitle().isEmpty()) {
            spec = spec.and(titleSpecificationProvider.getSpecification(params.getTitle()));
        }

        if (params.getAuthor() != null && !params.getAuthor().isEmpty()) {
            spec = spec.and(authorSpecificationProvider.getSpecification(params.getAuthor()));
        }

        if (params.getIsbn() != null && !params.getIsbn().isEmpty()) {
            spec = spec.and(isbnSpecificationProvider.getSpecification(params.getIsbn()));
        }

        if (params.getMinPrice() != null) {
            spec = spec.and(minPriceSpecificatorProvider.getSpecification(params.getMinPrice()
                    .toString()));
        }

        if (params.getMaxPrice() != null) {
            spec = spec.and(maxPriceSpecificationProvider.getSpecification(params.getMaxPrice()
                    .toString()));
        }

        return spec;
    }
}
