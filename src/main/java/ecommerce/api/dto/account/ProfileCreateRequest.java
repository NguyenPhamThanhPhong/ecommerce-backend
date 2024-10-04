package ecommerce.api.dto.account;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ProfileCreateRequest {
    private String fullName;

    private Short age;

    private String avatarUrl;


    private Integer phone;

    private Date dateOfBirth;
}
