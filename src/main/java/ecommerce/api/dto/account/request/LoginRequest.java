package ecommerce.api.dto.account.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class LoginRequest {
    @Length(min = 6, max = 40)
    private String username;
    @Length(min = 6, max = 40)
    private String password;
}
