package ecommerce.api.dto.product.request;

import ecommerce.api.entity.discount.Discount;
import ecommerce.api.entity.product.Brand;
import ecommerce.api.entity.product.Category;
import ecommerce.api.validation.annotation.AccountValidation;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequest {

    private String name;


    private String sku;


    private MultipartFile image;

    private Integer quantity;

    private Integer sold;

    private String description;

    private BigDecimal price;

    private Integer stock;

    private Map<String, Object> attributes;

   private String brandName;

   private String categoryName;

    private String productNo;
}
