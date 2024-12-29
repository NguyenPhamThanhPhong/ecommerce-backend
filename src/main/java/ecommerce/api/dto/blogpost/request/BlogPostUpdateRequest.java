package ecommerce.api.dto.blogpost.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ecommerce.api.validation.annotation.IdentityValidation;
import ecommerce.api.validation.criteria.IdentityCriteria;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
@IdentityValidation(message = "please Provide a valid token")
public class BlogPostUpdateRequest implements IdentityCriteria {
    @Override
    public UUID getIdentity() {
        return authorId;
    }
    @Override
    public void setIdentity(UUID identity) {
        this.authorId = identity;
    }

    private UUID id;
    private String title;

    private String subtitle;

    private MultipartFile image;

    @JsonIgnore
    private UUID authorId;

    private String content;

    private Boolean isHtml;

    private boolean isDraft;
}
