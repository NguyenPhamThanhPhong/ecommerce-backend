package ecommerce.api.dto.account.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegistrationRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    private ProfileCreateRequest request;
}
