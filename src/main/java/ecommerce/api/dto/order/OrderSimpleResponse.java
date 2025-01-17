package ecommerce.api.dto.order;

import ecommerce.api.dto.payment.PaymentResponse;
import lombok.Data;

import java.util.Date;

@Data
public class OrderSimpleResponse {
    private String id;

    private long code;

    private Date createdAt;

    private Date deletedAt;

    private Double totalValue;
}
