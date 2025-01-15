package ecommerce.api.dto.product.request;

import ecommerce.api.constants.ProductStatus;
import ecommerce.api.entity.product.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
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
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date availableDate;

    private List<MultipartFile> productImages;

    private MultipartFile thumbnail;
    private BigDecimal discountPercent;
    private Integer quantity;

    private BigDecimal price;

    private Integer stock;

    private String description;

}
