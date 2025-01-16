package ecommerce.api.dto.category.response;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class CategoryResponse {
    private UUID id;

    private long code;

    private Date createdAt;
    private Date deletedAt;

    private String description;

    private String name;

    private String imageUrl;

    private int stock;

    private int sold;


}
