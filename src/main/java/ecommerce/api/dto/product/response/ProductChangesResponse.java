package ecommerce.api.dto.product.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ProductChangesResponse {
    private UUID id;
    private long code;
    private String thumbnailUrl;
    private List<ProductImageResponse> productImages;
}
