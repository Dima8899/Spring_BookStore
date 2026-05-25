package mate.academy.library.specification.provider;

import java.math.BigDecimal;
import mate.academy.library.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class MinPriceSpecificatorProvider implements SpecificationProvider<Book> {
    private static final String PRICE = "price";

    @Override
    public Specification<Book> getSpecification(String param) {
        BigDecimal minPrice = new BigDecimal(param);
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get(PRICE), minPrice);
    }
}
