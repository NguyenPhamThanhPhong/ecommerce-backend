package ecommerce.api.dto.order;

import ecommerce.api.dto.product.response.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailResponse {
    private ProductResponse product;
    private int quantity;
}
