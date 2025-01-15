package ecommerce.api.dto.payment;

import ecommerce.api.constants.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PaymentResponse {
    private UUID accountId = UUID.randomUUID();

    private PaymentStatus status;

    private String paymentMethod;

    private BigDecimal amount;
}
