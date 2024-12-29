package ecommerce.api.dto.product.request;

import ecommerce.api.constants.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequest {

    private String name;

    private UUID brandId;

    private UUID categoryId;

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
