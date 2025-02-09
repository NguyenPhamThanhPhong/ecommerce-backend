package ecommerce.api.dto.blogpost.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ecommerce.api.validation.annotation.IdentityValidation;
import ecommerce.api.validation.criteria.IdentityCriteria;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
@IdentityValidation(message = "please Provide a valid token")
public class BlogPostCreateRequest implements IdentityCriteria {
    private String title;

    private MultipartFile image;

    private String subtitle;
    @JsonIgnore
    private UUID authorId;

    private String content;

    private Boolean isHtml;

//    public BlogPostCreateRequest() {authorId = UUID.randomUUID();}

    @Override
    public UUID getIdentity() {
        return authorId;
    }

    @Override
    public void setIdentity(UUID identity) {
        this.authorId = identity;
    }
}
