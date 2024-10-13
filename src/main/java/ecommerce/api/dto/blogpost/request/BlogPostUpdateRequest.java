package ecommerce.api.dto.blogpost.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
public class BlogPostUpdateRequest {
    private UUID id;
    private String title;
    private MultipartFile image;

    private String subtitle;

    private String authorId;

    private String content;

    private Boolean isHtml;
}
