package ecommerce.api.dto.product.request;

import ecommerce.api.constants.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateRequest {
    private UUID id;

    private String name;

    private UUID brandId;

    private UUID categoryId;

    private ProductStatus status;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date availableDate;

    private List<MultipartFile> appendingImages;
    private List<Integer> removalImageIds;

    private MultipartFile thumbnail;

    private BigDecimal discountPercent;

    private Integer quantity;

    private BigDecimal price;

    private Integer stock;

    private String description;
}
