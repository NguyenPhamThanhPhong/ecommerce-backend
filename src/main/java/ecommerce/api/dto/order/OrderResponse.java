package ecommerce.api.dto.order;

import ecommerce.api.constants.OrderStatus;
import ecommerce.api.dto.account.response.ProfileResponse;
import ecommerce.api.dto.coupon.response.CouponResponse;
import ecommerce.api.entity.coupon.Coupon;
import ecommerce.api.entity.transaction.OrderDetail;
import ecommerce.api.entity.user.Profile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private String id;

    private long code;

    private Date createdAt;

    private Date deletedAt;

    private String address;

    private String status;

    private String notes;

    private List<CouponResponse> coupons;

    private ProfileResponse profile;

    private List<OrderDetailResponse> orderDetails;

    private Double totalValue;
}
