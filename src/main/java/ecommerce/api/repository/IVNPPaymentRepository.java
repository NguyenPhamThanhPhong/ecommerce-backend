package ecommerce.api.repository;

import ecommerce.api.dto.projection.VNPUpdateDTO;
import ecommerce.api.entity.transaction.payment.VNPPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface IVNPPaymentRepository extends JpaRepository<VNPPayment, UUID> {



    @Modifying
    @Transactional
    @Query(value = """
            UPDATE vnpay_payments
            SET  bank_code = :#{#updateDTO.bankCode},
                card_method = :#{#updateDTO.cardMethod},
                order_info = :#{#updateDTO.orderInfo},
                trans_no = :#{#updateDTO.transNo},
                secure_hash = :#{#updateDTO.secureHash}
            WHERE trans_ref = :#{#updateDTO.transRef}
            """, nativeQuery = true)
    int updateVnpPayment(@Param("updateDTO") VNPUpdateDTO updateDTO);

    @Modifying
    @Query(value = """
        insert into vnpay_payments (id, trans_ref, order_info)
        values (:id, :transRef, :orderInfo)
        on conflict (id) do update
        set trans_ref = :transRef, order_info = :orderInfo
        """, nativeQuery = true)
    void upsert(UUID id, String transRef, String orderInfo);
}
