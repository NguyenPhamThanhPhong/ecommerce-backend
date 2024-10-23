package ecommerce.api.dto.product.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;


@Data
public class ProductResponse {
    private String name;


    private String sku;


    private String imageUrl;

    private Integer quantity;

    private Integer sold;

    private String description;

    private BigDecimal price;

    private Integer stock;

    private Map<String, Object> attributes;

    private CategoryResponse category;

    private BrandResponse brand;

    private String productNo;
}
