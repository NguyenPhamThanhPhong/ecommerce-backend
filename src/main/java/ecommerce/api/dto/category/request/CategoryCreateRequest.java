package ecommerce.api.dto.category.request;

import lombok.Data;

import java.util.List;

@Data
public class CategoryCreateRequest {

    private String description;

    private String name;

    private String parentCategoryString;

}
