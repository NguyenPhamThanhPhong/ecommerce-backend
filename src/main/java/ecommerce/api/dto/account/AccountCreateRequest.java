package ecommerce.api.dto.account;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreateRequest {
    private Date enableDate;
    private Date disableDate;
    @Email
    private String email;
    private String password;
    private String loginId;
    private Boolean isVerified;

    private Byte temp;

    private String role;

    private ProfileCreateRequest profile;
}
