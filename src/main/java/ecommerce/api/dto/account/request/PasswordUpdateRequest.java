package ecommerce.api.dto.account.request;

import lombok.Data;

@Data
public class PasswordUpdateRequest {
    private String otp;
    private String password;
}
