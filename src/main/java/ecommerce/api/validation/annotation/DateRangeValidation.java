package ecommerce.api.validation.annotation;

import ecommerce.api.validation.validator.AccountIdentityValidator;
import ecommerce.api.validation.validator.DateRangeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {DateRangeValidator.class})
@Target({ElementType.TYPE,ElementType.FIELD,ElementType.PARAMETER})  // Target the class (DTO)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateRangeValidation {
    String message() default "Invalid request data";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
