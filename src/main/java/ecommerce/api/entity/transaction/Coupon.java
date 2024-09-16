package ecommerce.api.entity.transaction;

import ecommerce.api.constants.CouponStatus;

import java.util.Date;

public class Coupon {
    private String code;
    private float discount;
    private float maxDiscount;
    private float minOrder;
    private Date startDate;
    private Date expiryDate;
    private CouponStatus status;
}
