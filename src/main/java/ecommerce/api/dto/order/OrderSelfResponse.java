package ecommerce.api.dto.order;


import ecommerce.api.dto.account.response.ProfileResponse;
import ecommerce.api.dto.payment.PaymentResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSelfResponse {
    private String id;

    private long code;

    private Date createdAt;

    private Date deletedAt;

    private String address;

    private String notes;

    private Double totalValue;

    private PaymentResponse payment;
}
