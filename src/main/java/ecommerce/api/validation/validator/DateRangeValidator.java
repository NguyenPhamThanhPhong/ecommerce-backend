package ecommerce.api.validation.validator;

import ecommerce.api.validation.annotation.DateRangeValidation;
import ecommerce.api.validation.criteria.DateRangeCriteria;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateRangeValidator implements ConstraintValidator<DateRangeValidation, DateRangeCriteria> {
    @Override
    public boolean isValid(DateRangeCriteria value, ConstraintValidatorContext context) {
        if (value.getEnableDate() == null || value.getDisableDate() == null) {
            return false;
        }
        return value.getEnableDate().before(value.getDisableDate());
    }
}
