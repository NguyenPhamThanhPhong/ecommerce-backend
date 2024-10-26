package ecommerce.api.dto.brand.response;

import lombok.Data;

import java.util.UUID;

@Data
public class BrandResponse {

    private UUID id;

    private String description;

    private String imageUrl;

    private String name;

}
