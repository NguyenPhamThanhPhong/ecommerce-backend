package ecommerce.api.entity.transaction;

import ecommerce.api.constants.OrderStatus;
import ecommerce.api.entity.base.EntityBase;
import ecommerce.api.entity.discount.Discount;
import ecommerce.api.entity.transaction.payment.Payment;

import ecommerce.api.entity.user.Profile;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "orders")
public class Order extends EntityBase {
    @Column(name = "address")
    private String address;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "discount_id", insertable = false, updatable = false)
    private UUID discountId;

    @Column(name = "issuer_id", insertable = false, updatable = false)
    private UUID profileId;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH,CascadeType.DETACH})
    @JoinColumn(name = "discount_id")
    private Discount discount;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @JoinColumn(name = "issuer_id")
    @Transient
    private Profile profile;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "order_id")
    private Set<OrderDetail> orderDetails = new LinkedHashSet<>();
}

