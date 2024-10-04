package ecommerce.api.entity.discount;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "fixed_discounts")
@PrimaryKeyJoinColumn(name = "id")
public class FixedDiscount extends Discount {

    @Column(name = "min_condition")
    private float minCondition;

    @Column(name = "fixed_discount")
    private float fixedDiscount;

}
