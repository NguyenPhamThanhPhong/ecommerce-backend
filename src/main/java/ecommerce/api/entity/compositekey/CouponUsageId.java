package ecommerce.api.entity.compositekey;

import lombok.Data;

@Data
public class CouponUsageId {
    private String accountId;
    private String couponId;
}
