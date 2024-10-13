package ecommerce.api.validation.validator;

import ecommerce.api.dto.general.UserDetailDTO;
import ecommerce.api.validation.annotation.IdentityValidation;
import ecommerce.api.validation.criteria.IdentityCriteria;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class IdentityValidator implements ConstraintValidator<IdentityValidation, IdentityCriteria> {
    @Override
    public boolean isValid(IdentityCriteria value, ConstraintValidatorContext context) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailDTO userDetailDTO = (UserDetailDTO) authentication.getPrincipal();
        if (userDetailDTO == null) {
            return false;
        } else if (userDetailDTO.getId() != value.getIdentity())
            return false;
        value.setIdentity(userDetailDTO.getId());
        return true;
    }
}
