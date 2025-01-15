package ecommerce.api.dto.account.request;

import ecommerce.api.constants.AccountRolesEnum;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class AccountUpdateRequest {
    private UUID id;
    private String email;
    private AccountRolesEnum role;
    private String password;
    private Date enableDate;
    private Date disableDate;

    private String isVerified;
    private ProfileUpdateRequest profile;
}
