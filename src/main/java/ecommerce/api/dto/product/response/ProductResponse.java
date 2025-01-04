package ecommerce.api.dto.product.response;

import ecommerce.api.constants.ProductStatus;
import ecommerce.api.dto.brand.response.BrandResponse;
import ecommerce.api.dto.category.response.CategoryResponse;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


@Data
public class ProductResponse {
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

    private Set<String> imageUrls;

    private BigDecimal discountPercent;

    private BigDecimal rating;

    private Integer quantity;

    private Integer sold;

    private BigDecimal price;

    private Integer stock;

    private String description;
}
