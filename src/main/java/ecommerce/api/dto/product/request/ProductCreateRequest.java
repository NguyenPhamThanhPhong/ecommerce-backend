package ecommerce.api.dto.product.request;

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

    private String sku;

    private MultipartFile image;

    private Integer quantity;

    private Integer sold;

    private String description;

    private BigDecimal price;

    private Integer stock;

    private Map<String , Object> attributes;

}
