package ecommerce.api.dto.category.request;

import lombok.Data;

import java.util.UUID;


@Data
public class CategoryUpdateRequest {

    private UUID id;

    private String description;

    private String name;

}
