package ecommerce.api.entity.discount;

import ecommerce.api.entity.product.Product;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "promotion_discounts")
@PrimaryKeyJoinColumn(name = "id")
public class PromotionDiscount extends Discount {

    @Column(name = "required_quantity")
    private Integer requiredQuantity;

    @Column(name = "reward_product_id")
    private String rewardProductId;

    @Column(name = "reward_quantity")
    private Integer rewardQuantity;

//IGNORE WHEN INSERT BUT FETCH WHEN SELECT
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH,CascadeType.REFRESH})
    @JoinColumn(name = "reward_product_id", insertable = false, updatable = false)
    private Product rewardProduct;

}
