package ecommerce.api.validation.validator;

import ecommerce.api.constants.AccountRolesEnum;
import ecommerce.api.dto.account.request.AccountCreateRequest;
import ecommerce.api.validation.annotation.AccountValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AccountIdentityValidator implements ConstraintValidator<AccountValidation, AccountCreateRequest> {

    @Override
    public void initialize(AccountValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(AccountCreateRequest value, ConstraintValidatorContext context) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            if (value.getRole() != AccountRolesEnum.ROLES_CUSTOMER) {
                return false;
            }
        }
        if (authentication instanceof UsernamePasswordAuthenticationToken
                && authentication.getPrincipal() == null) {
            return false;
        }
        return value.getEnableDate().before(value.getDisableDate());
    }
}
