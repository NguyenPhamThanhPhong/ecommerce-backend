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

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "orders")
public class Order extends EntityBase<Integer> {
    @Column(name = "address")
    private String address;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "discount_id", insertable = false, updatable = false)
    private Integer discountId;

    @Column(name = "profile_id", insertable = false, updatable = false)
    private String profileId;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH,CascadeType.DETACH})
    @JoinColumn(name = "discount_id")
    private Discount discount;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @JoinColumn(name = "profile_id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH,CascadeType.DETACH})
    private Profile profile;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "order")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<OrderDetail> orderDetails = new LinkedHashSet<>();

}

