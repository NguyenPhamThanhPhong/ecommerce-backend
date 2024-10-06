package ecommerce.api.dto.account;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.multipart.MultipartFile;

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

    private String role;
    private ProfileCreateRequest profile;

    private MultipartFile file;
}
