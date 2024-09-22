package ecommerce.api.entity.transaction.payment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Data
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "cash_payments")
public class CashPayment extends Payment {
    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "paid", precision = 10, scale = 2)
    private BigDecimal paid;

    @Column(name = "exchange", precision = 10, scale = 2)
    private BigDecimal exchange;

    @Column(name = "cash_method", length = Integer.MAX_VALUE)
    private String cashMethod;

}
