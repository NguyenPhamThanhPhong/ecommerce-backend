package ecommerce.api.entity.coupon;


import ecommerce.api.entity.compositekey.CouponUsageId;
import ecommerce.api.entity.compositekey.OrderDetailId;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Data
@Table(name = "coupon_usages")
@IdClass(CouponUsageId.class)
public class CouponUsage {
    @Id
    @Column(name = "account_id")
    private UUID accountId;
    @Id
    @Column(name = "coupon_id")
    private UUID couponId;
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "times")
    private Integer times;

}
