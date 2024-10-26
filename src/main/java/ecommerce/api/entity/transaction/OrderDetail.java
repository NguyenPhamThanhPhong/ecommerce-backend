package ecommerce.api.entity.transaction;

import ecommerce.api.entity.compositekey.OrderDetailId;
import ecommerce.api.entity.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "order_details")
@IdClass(OrderDetailId.class)
public class OrderDetail {
    @Id
    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Id
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "gross_total", precision = 10, scale = 2)
    private BigDecimal grossTotal;

    @Column(name = "net_total", precision = 10, scale = 2)
    private BigDecimal netTotal;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

}
