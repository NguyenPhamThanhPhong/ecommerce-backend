package ecommerce.api.dto.account;

import lombok.Data;

import java.time.Instant;

@Data
public class ProfileResponse {
    private String fullName;

    private String avatarUrl;

    private String phone;

    private Instant dateOfBirth;
}
