package ecommerce.api.controller;

import ecommerce.api.service.business.PaymentService;
import ecommerce.api.service.business.VNPService;
import ecommerce.api.config.payment.VNPayUtil;
import ecommerce.api.dto.payment.VNPPaymentRequest;
import ecommerce.api.dto.payment.VNPPaymentUrlRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {
    private final VNPService vnpService;
    private final PaymentService paymentService;

    @PostMapping("/vnpay-url")
    public ResponseEntity<?> getUrl(@RequestBody VNPPaymentUrlRequest req, HttpServletRequest request) throws IOException {
        String ip = VNPayUtil.getIpAddress(request);
        return ResponseEntity.ok(vnpService.createVnPayPayment(req,ip));
    }

    @PutMapping(value = "/vnpay",produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> vnpayPayment(@RequestBody VNPPaymentRequest request) {
        String message = paymentService.updatePaymentTransaction(request);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/cash")
    public String cashPayment() {
        return "Cash payment";
    }
}
