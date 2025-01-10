package ecommerce.api.entity.transaction.payment;

import ecommerce.api.constants.PaymentStatus;
import ecommerce.api.entity.base.EntityBase;
import ecommerce.api.entity.transaction.Order;
import ecommerce.api.entity.user.Account;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "payments")
@DiscriminatorColumn(name = "payment_method")
public class Payment extends EntityBase {
    @Column(name = "account_id")
    private UUID accountId = UUID.randomUUID();

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "payment_method",insertable = false, updatable = false)
    private String paymentMethod;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account account;
}
