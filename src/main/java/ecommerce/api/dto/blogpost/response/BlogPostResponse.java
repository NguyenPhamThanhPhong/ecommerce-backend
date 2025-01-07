package ecommerce.api.dto.blogpost.response;

import ecommerce.api.dto.account.response.ProfileResponse;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class BlogPostResponse {
    private UUID id;
    private long code;

    private Date createdAt;
    private Date deletedAt;
    private String title;
    private String subtitle;
    private String imageUrl;
    private String content;
    private Boolean isHtml;
    private ProfileResponse author;
}
