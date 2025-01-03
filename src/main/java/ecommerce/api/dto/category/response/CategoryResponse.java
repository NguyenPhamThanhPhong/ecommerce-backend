package ecommerce.api.dto.category.response;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CategoryResponse {
    private UUID id;

    private long code;

    private String description;

    private String name;
}
