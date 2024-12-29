package ecommerce.api.dto.product.response;

import ecommerce.api.constants.ProductStatus;
import ecommerce.api.dto.brand.response.BrandResponse;
import ecommerce.api.dto.category.response.CategoryResponse;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.UUID;


@Data
public class ProductResponse {
    private String id;

    private String name;

    private BrandResponse brand;

    private CategoryResponse category;

    private ProductStatus status;

    private Date availableDate;

    private Map<String, MultipartFile> images;

    private BigDecimal discountPercent;

    private Integer quantity;

    private Integer sold;

    private BigDecimal price;

    private Integer stock;

    private String description;
}
