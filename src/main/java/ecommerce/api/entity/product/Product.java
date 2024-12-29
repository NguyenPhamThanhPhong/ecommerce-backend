package ecommerce.api.entity.product;

import ecommerce.api.constants.ProductStatus;
import ecommerce.api.entity.base.EntityBase;
import ecommerce.api.entity.transaction.OrderDetail;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "products")
public class Product extends EntityBase {
    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Column(name = "brand_id")
    private UUID brandId;

    @Column(name = "category_id")
    private UUID categoryId;

    @Size(max = 2048)
    @Column(name = "image_urls")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, String> imageUrls;

    @Column(name = "rating",precision = 1, scale = 1)
    private BigDecimal rating;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Column(name = "available_date")
    private Date availableDate;

    @Column(name = "sold")
    private Integer sold;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "discount_percent", precision = 3, scale = 2)
    private BigDecimal discountPercent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id", insertable = false, updatable = false)
    @Fetch(FetchMode.JOIN)
    private Brand brand;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    @Fetch(FetchMode.JOIN)
    private Category category;


    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false,updatable = false)
    private Set<OrderDetail> orderDetails = new HashSet<>();

    public void appendImageUrl(String key, String url) {
        if (imageUrls == null) {
            imageUrls = new HashMap<>();
        }
        imageUrls.put(key, url);
    }
    public void removeImageUrl(String key) {
        if (imageUrls != null) {
            imageUrls.remove(key);
        }
    }



}
