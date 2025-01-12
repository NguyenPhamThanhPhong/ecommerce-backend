package ecommerce.api.service.business;

import ecommerce.api.dto.payment.VNPPaymentRequest;
import ecommerce.api.dto.projection.VNPUpdateDTO;
import ecommerce.api.exception.BadRequestException;
import ecommerce.api.mapper.PaymentMapper;
import ecommerce.api.repository.IPaymentRepository;
import ecommerce.api.repository.IVNPPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final IVNPPaymentRepository vnpPaymentRepository;
    private final IPaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Transactional
    public String updatePaymentTransaction(VNPPaymentRequest request){
        VNPUpdateDTO dto = paymentMapper.fromVNPCallbackToEntity(request);
        if (!Objects.equals(request.getResponseCode(), "00")){
            paymentRepository.updatePaymentFAILED(dto);
            return "Payment failed";
        }
        int vnpChanges = vnpPaymentRepository.updateVnpPayment(dto);
        int paymentChanges = paymentRepository.updatePaymentPAID(dto);
        if (vnpChanges !=1 || paymentChanges != 1){
            throw new BadRequestException("Payment update failed");
        }
        return "Payment success";
    }
}
