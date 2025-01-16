package ecommerce.api.entity.transaction;

import ecommerce.api.entity.base.EntityBase;
import ecommerce.api.entity.coupon.Coupon;
import ecommerce.api.entity.transaction.payment.Payment;
import ecommerce.api.entity.user.Profile;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity @Getter @Setter
@Table(name = "orders")
public class Order extends EntityBase {
    @Column(name = "address")
    private String address;

    @Column(name = "notes", length = 200)
    private String notes;

    @Column(name = "creator_id")
    private UUID creatorId;

    @Column(name = "coupon_id")
    private UUID couponId;

    @Column(name = "total_value")
    private BigDecimal totalValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false, updatable = false)
    private Coupon coupon;

    @OneToOne(mappedBy = "order")
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", insertable = false, updatable = false)
    private Profile profile;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = {})
    private List<OrderDetail> orderDetails;
}

