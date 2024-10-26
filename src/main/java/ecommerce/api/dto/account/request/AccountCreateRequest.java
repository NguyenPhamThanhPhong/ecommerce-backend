package ecommerce.api.dto.account.request;

import ecommerce.api.validation.criteria.DateRangeCriteria;
import ecommerce.api.validation.annotation.AccountValidation;
import ecommerce.api.constants.AccountRolesEnum;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@AccountValidation
public class AccountCreateRequest implements DateRangeCriteria {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date enableDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date disableDate;
    @Email
    private String email;
    @Length(min = 6, max = 40)
    private String password;
    @Length(min = 6, max = 40)
    private String loginId;
    private Boolean isVerified;
    private AccountRolesEnum role;
    private ProfileCreateRequest profile;
}
