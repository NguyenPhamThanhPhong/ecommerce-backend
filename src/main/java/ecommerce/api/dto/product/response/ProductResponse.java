package ecommerce.api.dto.product.response;

import ecommerce.api.dto.brand.response.BrandResponse;
import ecommerce.api.dto.category.response.CategoryResponse;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;


@Data
public class ProductResponse {

    private UUID id;

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

    private BigDecimal discountPercent;
}
