package ecommerce.api.controller;

import ecommerce.api.config.payment.VNPService;
import ecommerce.api.config.payment.VNPayUtil;
import ecommerce.api.dto.payment.VNPPaymentRequest;
import ecommerce.api.dto.payment.VNPPaymentUrlRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {
    private final VNPService vnpService;

    @GetMapping("/vnpay-url")
    public ResponseEntity<?> getUrl(@ParameterObject VNPPaymentUrlRequest req, HttpServletRequest request) throws IOException {
        String ip = VNPayUtil.getIpAddress(request);
        return ResponseEntity.ok(vnpService.createVnPayPayment(req,ip));
    }

    @PostMapping("/vnpay")
    public String vnpayPayment(@RequestBody VNPPaymentRequest request) {
        return "VNPay payment";
    }

    @PostMapping("/cash")
    public String cashPayment() {
        return "Cash payment";
    }
}
