package ecommerce.api.dto.account.response;

import ecommerce.api.constants.AccountRolesEnum;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class AccountResponse {
    private UUID id ;

    private long code;

    private String email;

    private Date enableDate;

    private Date disableDate;

    private Boolean isVerified;

    private AccountRolesEnum role;
    
    private ProfileResponse profile;
}
