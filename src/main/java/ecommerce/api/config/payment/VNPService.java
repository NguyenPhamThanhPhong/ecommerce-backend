package ecommerce.api.config.payment;


import ecommerce.api.dto.payment.PaymentURLResponse;
import ecommerce.api.dto.payment.VNPPaymentUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class VNPService {
    private final VNPConfig vnPayConfig;
    public PaymentURLResponse createVnPayPayment(VNPPaymentUrlRequest req, String ip) {
        long amount = req.getAmount() * 100L;
        String bankCode = req.getBankCode();
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", ip);
        //build query url
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
        return PaymentURLResponse.builder()
                .message("success")
                .url(paymentUrl).build();
    }
}