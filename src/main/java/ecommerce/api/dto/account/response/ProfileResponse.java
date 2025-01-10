package ecommerce.api.dto.account.response;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class ProfileResponse {
    private String fullName;

    private String avatarUrl;

    private String phone;

    private Date dateOfBirth;

    private String primaryAddress;

    private Map<String,String> addresses;

}
