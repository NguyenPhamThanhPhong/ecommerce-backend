package ecommerce.api.entity.user;

import ecommerce.api.entity.base.EntityBase;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;

import java.util.Date;

public class AuthToken extends EntityBase<String> {
    private String accountId;
    private String accessToken;
    private String refreshToken;
    private Date accessExpiry;
    private Date refreshExpiry;

    @OneToOne(fetch = FetchType.LAZY)
    @Transient
    private Account account;
}
