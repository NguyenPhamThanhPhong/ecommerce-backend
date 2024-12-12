package ecommerce.api.dto.coupon.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
public class CouponResponse {

    private UUID id;

    private String couponCode;


    private BigDecimal usageLimit;


    private String imageUrl;


    private String description;


    private String couponType;


    private BigDecimal value;


    private Date startDate;

    private Date endDate;
}
