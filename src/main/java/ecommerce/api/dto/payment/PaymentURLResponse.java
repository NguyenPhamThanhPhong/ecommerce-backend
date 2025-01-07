package ecommerce.api.dto.payment;

import lombok.Builder;
@Builder
public class PaymentURLResponse {
    public String message;
    public String url;
}