package ecommerce.api.constants;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class AuthRoleConstants {
    public static final String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_STAFF = "ROLE_STAFF";
    public static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";

    public static final String ROLE_DEFAULT = "ROLE_DEFAULT";
    public static final String ROLE_ADD = "ROLE_ADD";
    public static final String ROLE_DELETE = "ROLE_DELETE";
    public static final String ROLE_UPDATE = "ROLE_UPDATE";
    public static final String ROLE_VIEW_ACCOUNT = "ROLE_VIEW_ACCOUNT";
    public static final String ROLE_VIEW_SALARY = "ROLE_VIEW_SALARY";

    public static String[] processRoles(String roles){
        return switch (roles) {
            case ROLE_ADMIN -> ROLE_ADMINS;
            case ROLE_STAFF -> ROLE_STAFFS;
            case ROLE_CUSTOMER -> ROLE_CUSTOMERS;
            case ROLE_SUPER_ADMIN -> ROLE_SUPER_ADMINS;
            default -> roles.split(",");
        };
    }

    public static final String[] ROLE_ADMINS = {
            ROLE_ADMIN,
            ROLE_ADD,
            ROLE_DEFAULT,
            ROLE_VIEW_ACCOUNT,
            ROLE_VIEW_SALARY,
            ROLE_DELETE,
            ROLE_UPDATE
    };
    public static final String[] ROLE_STAFFS = {
            ROLE_DEFAULT,
            ROLE_VIEW_SALARY
    };
    public static final String[] ROLE_CUSTOMERS = {
            ROLE_DEFAULT
    };
    public static final String[] ROLE_SUPER_ADMINS = {
            ROLE_SUPER_ADMIN,
            ROLE_ADMIN,
            ROLE_STAFF,
            ROLE_CUSTOMER,
            ROLE_ADD,
            ROLE_DELETE,
            ROLE_UPDATE,
            ROLE_VIEW_ACCOUNT,
            ROLE_VIEW_SALARY
    };
}
