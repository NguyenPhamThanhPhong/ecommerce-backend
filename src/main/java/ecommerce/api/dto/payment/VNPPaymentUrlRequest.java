package ecommerce.api.dto.payment;

import lombok.Data;

@Data
public class VNPPaymentUrlRequest {
    private int amount;
    private String bankCode;
    private String language;
}
