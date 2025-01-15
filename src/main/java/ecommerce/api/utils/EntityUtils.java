package ecommerce.api.utils;

import ecommerce.api.constants.PaymentStatus;
import ecommerce.api.entity.transaction.payment.Payment;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

public class EntityUtils {
    public static Payment newPayment(UUID accountId, UUID orderId, BigDecimal amount) {
        Payment payment = new Payment();
        payment.setAccountId(accountId);
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        return payment;
    }
    public static String getRandomColorHex() {
        Random random = new Random();
        int color = random.nextInt(0xFFFFFF + 1); // Generate a number between 0 and 0xFFFFFF
        return String.format("#%06X", color); // Format as hex string
    }
}
