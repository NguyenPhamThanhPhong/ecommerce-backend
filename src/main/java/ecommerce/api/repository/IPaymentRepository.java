package ecommerce.api.repository;

import ecommerce.api.entity.transaction.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPaymentRepository extends JpaRepository<Payment, Long> {
}
