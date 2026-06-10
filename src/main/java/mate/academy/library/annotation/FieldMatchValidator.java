package mate.academy.library.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class FieldMatchValidator
        implements ConstraintValidator<FieldMatch, Object> {

    private String field;
    private String fieldMatch;

    @Override
    public void initialize(FieldMatch annotation) {
        this.field = annotation.field();
        this.fieldMatch = annotation.fieldMatch();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext ctx) {
        Object fieldValue = new BeanWrapperImpl(value)
                .getPropertyValue(field);
        Object fieldMatchValue = new BeanWrapperImpl(value)
                .getPropertyValue(fieldMatch);

        if (fieldValue == null) {
            return fieldMatchValue == null;
        }
        return fieldValue.equals(fieldMatchValue);
    }
}
