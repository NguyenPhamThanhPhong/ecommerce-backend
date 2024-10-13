package ecommerce.api.dto.account.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ecommerce.api.validation.criteria.IdentityCriteria;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

@Data
public class ProfileUpdateRequest implements IdentityCriteria {
    @JsonIgnore
    private UUID id;
    private String fullName;
    private MultipartFile avatar;
    private Date dateOfBirth;
    private String phone;

    @Override
    public UUID getIdentity() {
        return id;
    }

    @Override
    public void setIdentity(UUID identity) {
        this.id = identity;
    }
}
