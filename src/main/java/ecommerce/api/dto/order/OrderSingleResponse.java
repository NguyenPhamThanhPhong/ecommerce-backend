package ecommerce.api.dto.order;

import ecommerce.api.dto.account.response.ProfileResponse;
import ecommerce.api.dto.coupon.response.CouponResponse;
import ecommerce.api.dto.payment.PaymentResponse;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderSingleResponse {
    private String id;
    private long code;
    private Date createdAt;
    private Date deletedAt;
    private String address;
    private String notes;
    private Double totalValue;
    private CouponResponse coupon;
    private PaymentResponse payment;
    private ProfileResponse profile;
    private List<OrderDetailResponse> orderDetails;
}
