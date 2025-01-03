package ecommerce.api.dto.blogpost.response;

import ecommerce.api.entity.user.Profile;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class BlogPostResponse {
    private UUID id;
    private long code;
    private String title;
    private Date createdAt;
    private Date deletedAt;
    private String subtitle;
    private String imageUrl;

    private UUID authorId;

    private String content;

    private Boolean isHtml;

    private Profile author;
}
