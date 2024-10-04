package ecommerce.api;

import ecommerce.api.entity.discount.Discount;
import ecommerce.api.entity.product.Product;
import ecommerce.api.entity.user.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "products_discounts")
public class ProductsDiscount {
    @EmbeddedId
    private ProductsDiscountId id;

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @MapsId("discountId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "discount_id", nullable = false)
    private Discount discount;

}