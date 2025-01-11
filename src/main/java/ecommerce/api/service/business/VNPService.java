package ecommerce.api.service.business;


import ecommerce.api.config.payment.VNPConfig;
import ecommerce.api.config.payment.VNPayUtil;
import ecommerce.api.constants.PaymentStatus;
import ecommerce.api.dto.payment.VNPPaymentUrlRequest;
import ecommerce.api.entity.transaction.payment.Payment;
import ecommerce.api.exception.BadRequestException;
import ecommerce.api.repository.IPaymentRepository;
import ecommerce.api.repository.IVNPPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class VNPService {
    private final VNPConfig vnPayConfig;
    private final IVNPPaymentRepository vnpRepository;
    private final IPaymentRepository paymentRepository;

    @Transactional
    public String createVnPayPayment(VNPPaymentUrlRequest req, String ip) {
        UUID orderId = req.getOrderId();
        Payment payment = paymentRepository.findByOrderIdAndStatus(orderId, PaymentStatus.PENDING)
                .orElseThrow(() -> new BadRequestException("ORDER NOT FOUND OR PAYMENT EXPIRED"));
        long amount = payment.getAmount().longValue() * 100;
        String transRef = orderId.toString();
        String bankCode = req.getBankCode();
        vnpRepository.upsert(payment.getId(), transRef, req.getOrderInfo());
        return buildQuery(bankCode, amount, transRef, req.getOrderInfo(), ip);
    }

    private String buildQuery(String bankCode, long amount, String transRef, String orderInfo, String ip) {
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig(transRef, orderInfo);
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
        return vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
    }
}