package ecommerce.api.entity.user;

import ecommerce.api.constants.AccountStatus;
import ecommerce.api.entity.base.EntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;


public class Account extends EntityBase<String> {
    private String email;
    private String password;
    private String loginId;
    private boolean isDeleted;
    private AccountStatus status;

    private List<String> roles;
    private String OTP;


    private Profile profile;
    private List<AuthToken> tokens;

}
