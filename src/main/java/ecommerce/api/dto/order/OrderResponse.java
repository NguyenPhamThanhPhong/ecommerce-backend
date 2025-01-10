package ecommerce.api.dto.order;

import ecommerce.api.dto.coupon.response.CouponResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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

    private Double totalValue;
}
