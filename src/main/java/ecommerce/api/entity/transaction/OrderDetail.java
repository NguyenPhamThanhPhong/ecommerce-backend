package ecommerce.api.entity.transaction;

import ecommerce.api.entity.base.EntityBase;
import ecommerce.api.entity.compositekey.OrderDetailId;
import ecommerce.api.entity.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter @Setter
@SuperBuilder
@NoArgsConstructor
@Table(name = "order_details")
@IdClass(OrderDetailId.class)
public class OrderDetail extends EntityBase {

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "product_id")
    private UUID productId;

    @ManyToOne(targetEntity = Order.class)
    @JoinColumn(name = "order_id", nullable = false,updatable = false,insertable = false)
    private Order order;

    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name = "product_id" , nullable = false,updatable = false,insertable = false)
    private Product product;

}
