package ecommerce.api.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public enum AccountRolesEnum {
    ROLE_ADMIN(Set.of(
            AuthRoleConstants.ROLE_ADMIN,
            AuthRoleConstants.ROLE_ADD,
            AuthRoleConstants.ROLE_DEFAULT,
            AuthRoleConstants.ROLE_VIEW_ACCOUNT,
            AuthRoleConstants.ROLE_VIEW_SALARY,
            AuthRoleConstants.ROLE_DELETE,
            AuthRoleConstants.ROLE_UPDATE
    )),
    ROLE_STAFF(Set.of(
            AuthRoleConstants.ROLE_DEFAULT,
            AuthRoleConstants.ROLE_VIEW_SALARY
    )),
    ROLES_CUSTOMER(Set.of(
            AuthRoleConstants.ROLE_DEFAULT
    ));
    private final Set<String> roles;
}

