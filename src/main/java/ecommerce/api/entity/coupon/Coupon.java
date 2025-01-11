package ecommerce.api.entity.coupon;

import ecommerce.api.constants.CouponType;
import ecommerce.api.entity.base.EntityBase;
import ecommerce.api.entity.transaction.Order;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.*;


@Entity
@Data
@NoArgsConstructor
@Table(name = "coupons")
public class Coupon {
    @Id
    protected UUID id = UUID.randomUUID();

    @Column(name = "code", insertable = true, updatable = true)
    private String code;

    @ColumnDefault("now()")
    @Column(name = "created_at", insertable = false, updatable = false)
    protected Date createdAt;

    @Column(name = "deleted_at")
    protected Date deletedAt;

    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Column(name = "current_usage")
    private Integer currentUsage;

    @Column(name = "description")
    private String description;

    @Column(name = "coupon_type")
    @Enumerated(EnumType.STRING)
    private CouponType couponType;

    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @OneToMany(mappedBy = "coupon")
    @Transient
    private List<Order> order;

}
