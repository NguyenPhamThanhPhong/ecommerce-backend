package ecommerce.api.utils;

import ecommerce.api.constants.PaymentStatus;
import ecommerce.api.entity.transaction.payment.Payment;

import java.math.BigDecimal;
import java.util.UUID;

public class EntityUtils {
    public static Payment newPayment(UUID accountId, UUID orderId, BigDecimal amount) {
        Payment payment = new Payment();
        payment.setAccountId(accountId);
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        return payment;
    }
}
