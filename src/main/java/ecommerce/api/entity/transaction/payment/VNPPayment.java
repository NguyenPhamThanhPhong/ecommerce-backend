package ecommerce.api.entity.transaction.payment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Data
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "vnpay_payments")
public class VNPPayment extends Payment{
    @Column(name = "bank_code", length = Integer.MAX_VALUE)
    private String bankCode;

    @Column(name = "order_info", length = Integer.MAX_VALUE)
    private String orderInfo;

    @Column(name = "card_method", length = Integer.MAX_VALUE)
    private String cardMethod;

    @Column(name = "trans_ref",length = 100)
    private String transRef;

    @Column(name = "secure_hash",length = 256)
    private String secureHash;

    @Column(name = "trans_no",length = 15)
    private String transNo;

}
