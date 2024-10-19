package ecommerce.api.dto.product;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Map;


@Data
public class ProductUpdateRequest {
    private String id;

    private String sku;

    private MultipartFile image;

    private Integer quantity;

    private Integer sold;

    private String description;

    private BigDecimal price;

    private Integer stock;

    private Map<String, Object> attributes;
}
