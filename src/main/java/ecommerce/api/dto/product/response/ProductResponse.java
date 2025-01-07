package ecommerce.api.dto.product.response;

import ecommerce.api.constants.ProductStatus;
import ecommerce.api.dto.brand.response.BrandResponse;
import ecommerce.api.dto.category.response.CategoryResponse;
import ecommerce.api.entity.product.ProductImage;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;


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

    private List<ProductImageResponse> productImages = new ArrayList<>();

    private BigDecimal discountPercent;

    private BigDecimal rating;

    private Integer quantity;

    private BigDecimal price;

    private Integer stock;

    private String description;
}
