package ecommerce.api.dto.account;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

@Data
public class ProfileUpdateRequest {
    private UUID id;
    private String fullName;
    private Short age;
    private MultipartFile avatar;
    private Date dateOfBirth;
    private String phone;
}
