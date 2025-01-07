package ecommerce.api.dto.payment;

import lombok.Data;

@Data
public class VNPPaymentRequest {
    private String userId;
    private int amount;
    private String orderInfo;
    private String bankCode;
}
