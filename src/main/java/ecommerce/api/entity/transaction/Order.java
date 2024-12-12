package ecommerce.api.entity.transaction;

import ecommerce.api.constants.OrderStatus;
import ecommerce.api.entity.base.EntityBase;
import ecommerce.api.entity.coupon.Coupon;

import ecommerce.api.entity.user.Profile;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedHashSet;
import java.util.Set;

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

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH,CascadeType.DETACH})
    private Set<Coupon> Coupon = new LinkedHashSet<>();


    @JoinColumn(name = "customer_id")
    @Transient
    private Profile profile;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private Set<OrderDetail> orderDetails = new LinkedHashSet<>();

    @Column(name = "total_value")
    private Double totalValue;
}

