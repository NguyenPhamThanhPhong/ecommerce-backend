package ecommerce.api.repository;

import ecommerce.api.constants.PaymentStatus;
import ecommerce.api.dto.payment.VNPPaymentRequest;
import ecommerce.api.dto.projection.VNPUpdateDTO;
import ecommerce.api.entity.transaction.payment.Payment;
import ecommerce.api.entity.transaction.payment.VNPPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface IPaymentRepository extends JpaRepository<Payment, UUID> {

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE payments
        SET status = :#{T(ecommerce.api.constants.PaymentStatus).PAID.name()},
            payment_method = 'Payment'
        WHERE order_id = CAST(:#{#updateDTO.transRef} AS UUID)
          AND amount = :#{#updateDTO.amount}
        """, nativeQuery = true)
    int updatePaymentPAID(@Param("updateDTO") VNPUpdateDTO updateDTO);

    @Query("""
            SELECT p from Payment p left join Order o on p.orderId = o.id
            WHERE p.orderId = :id and p.status = :status
            and o.deletedAt is null and p.deletedAt is null
            """)
    Optional<Payment> findByOrderIdAndStatus(UUID id, PaymentStatus status);

    @Modifying
    @Query(value = """
            UPDATE payments
            SET status = :#{T(ecommerce.api.constants.PaymentStatus).FAILED.name()}
            WHERE order_id = :#{#updateDTO.transRef} and amount = :#{#updateDTO.amount}
            """, nativeQuery = true)
    void updatePaymentFAILED(@Param("updateDTO") VNPUpdateDTO updateDTO);
}
