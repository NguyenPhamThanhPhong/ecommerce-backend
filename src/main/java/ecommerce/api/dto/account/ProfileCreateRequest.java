package ecommerce.api.dto.account;


import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@NoArgsConstructor
@Schema(description = "Profile information for the account")
public class ProfileCreateRequest {
    @Schema(description = "Full name of the user")
    private String fullName;

    @Schema(description = "Age of the user")
    private Short age;

    @Schema(type = "string", format = "binary", description = "User's avatar")
    private MultipartFile avatar; // File input for avatar

    @Schema(description = "Phone number")
    private Integer phone;

    @Schema(description = "Date of birth")
    private Date dateOfBirth;
}