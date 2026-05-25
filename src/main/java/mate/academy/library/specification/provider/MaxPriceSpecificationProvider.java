package mate.academy.library.specification.provider;

import java.math.BigDecimal;
import mate.academy.library.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class MaxPriceSpecificationProvider implements SpecificationProvider<Book> {
    private static final String PRICE = "price";

    @Override
    public Specification<Book> getSpecification(String param) {
        BigDecimal maxPrice = new BigDecimal(param);
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get(PRICE), maxPrice);
    }
}
