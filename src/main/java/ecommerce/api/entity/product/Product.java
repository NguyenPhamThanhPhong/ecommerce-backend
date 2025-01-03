package ecommerce.api.entity.product;

import ecommerce.api.constants.ProductStatus;
import ecommerce.api.entity.base.EntityBase;
import ecommerce.api.entity.transaction.OrderDetail;
import ecommerce.api.entity.user.Account;
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

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "rating", precision = 1, scale = 2)
    private BigDecimal rating;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Column(name = "available_date")
    private Date availableDate;

    @Column(name = "description")
    private String description;

    @Column(name = "highlights")
    private String highlights;

    @Column(name = "policies")
    private String policies;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "discount_percent", precision = 3, scale = 2)
    private BigDecimal discountPercent;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "brand_id", insertable = false, updatable = false)
    private Brand brand;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category category;

    @OneToMany(mappedBy = "product",targetEntity = ProductImage.class)
    private List<ProductImage> productImages = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false, updatable = false)
    private Set<OrderDetail> orderDetails = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "favorite_products",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id"))
    private Set<Account> users = new HashSet<>();
}
