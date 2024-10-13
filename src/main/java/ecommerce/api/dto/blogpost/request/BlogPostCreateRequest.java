package ecommerce.api.dto.blogpost.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BlogPostCreateRequest {
    private String title;

    private MultipartFile image;

    private String subtitle;

    private String authorId;

    private String content;

    private Boolean isHtml;
}
