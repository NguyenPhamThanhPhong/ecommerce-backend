package ecommerce.api.repository;

import ecommerce.api.constants.PaymentStatus;
import ecommerce.api.dto.payment.VNPPaymentRequest;
import ecommerce.api.dto.projection.VNPUpdateDTO;
import ecommerce.api.entity.transaction.payment.Payment;
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
            SET status = :#{T(ecommerce.api.constants.PaymentStatus).PAID}
            WHERE order_id = :#{#updateDTO.transRef} and amount = :#{#updateDTO.amount}
            """, nativeQuery = true)
    int updatePaymentPAID(@Param("updateDTO") VNPUpdateDTO updateDTO);
}
