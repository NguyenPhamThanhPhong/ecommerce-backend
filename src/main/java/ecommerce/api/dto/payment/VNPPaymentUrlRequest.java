package ecommerce.api.dto.payment;

import lombok.Data;

import java.util.UUID;

@Data
public class VNPPaymentUrlRequest {
    private UUID orderId;
    private String bankCode;
    private String orderInfo = "";
}
