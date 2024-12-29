package ecommerce.api.entity.transaction;

import ecommerce.api.constants.OrderStatus;
import ecommerce.api.entity.base.EntityBase;
import ecommerce.api.entity.coupon.Coupon;

import ecommerce.api.entity.transaction.payment.Payment;
import ecommerce.api.entity.user.Profile;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
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

    @Column(name = "notes", length = 200)
    private String notes;

    @Column(name = "creator_id")
    private UUID creatorId;

    @Column(name = "coupon_id")
    private UUID couponId;

    @Column(name = "total_value")
    private Double totalValue;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(insertable = false, updatable = false)
    private Set<Coupon> coupons = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER)
    private Payment payment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_id", insertable = false, updatable = false)
    private Profile profile;

    @OneToMany
    private Set<OrderDetail> orderDetails = new LinkedHashSet<>();


}

