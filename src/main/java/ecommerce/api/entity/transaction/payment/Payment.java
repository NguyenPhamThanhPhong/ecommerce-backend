package ecommerce.api.entity.transaction.payment;

import ecommerce.api.entity.base.EntityBase;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "payments")
@DiscriminatorColumn(name = "payment_method")
public class Payment extends EntityBase<Integer> {

    @Column(name = "account_id")
    private UUID accountId;

    @Column(name = "status", length = Integer.MAX_VALUE)
    private String status;

    @Column(name = "payment_method",insertable = false, updatable = false)
    private String paymentMethod;
}
