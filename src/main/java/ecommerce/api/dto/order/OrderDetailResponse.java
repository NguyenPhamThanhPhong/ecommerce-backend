package ecommerce.api.dto.order;

import ecommerce.api.constants.ProductStatus;
import ecommerce.api.dto.brand.response.BrandResponse;
import ecommerce.api.dto.category.response.CategoryResponse;
import ecommerce.api.dto.product.response.ProductImageResponse;
import ecommerce.api.dto.product.response.ProductResponse;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailResponse {
    private ProductSimpleResponse product;
    private int quantity;

    @Getter
    @Setter
    public static class ProductSimpleResponse{
        private String id;
        private long code;
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private Date createdAt;
        private String name;
        private BrandResponse brand;
        private CategoryResponse category;
        private ProductStatus status;
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private Date availableDate;
        private String thumbnailUrl;
        private BigDecimal discountPercent;
        private Integer quantity;
        private BigDecimal price;
        private Integer stock;
        private String description;
    }
}
