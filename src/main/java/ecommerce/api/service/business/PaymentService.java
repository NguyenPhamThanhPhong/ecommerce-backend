package ecommerce.api.service.business;

import ecommerce.api.dto.payment.VNPPaymentRequest;
import ecommerce.api.repository.IPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final IPaymentRepository paymentRepository;

    public void save(VNPPaymentRequest request){
//        paymentRepository.save()
    }
}
