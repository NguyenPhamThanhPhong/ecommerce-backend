package ecommerce.api.dto.product.response;
import lombok.Data;

import java.util.List;

@Data
public class CategoryResponse {
    private String description;


    private String name;

    private String parentCategory;

    private List<String> childrenCategories;
}
