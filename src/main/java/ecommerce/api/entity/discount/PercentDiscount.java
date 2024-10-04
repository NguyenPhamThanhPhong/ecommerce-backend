package ecommerce.api.entity.discount;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "percent_discounts")
@PrimaryKeyJoinColumn(name = "id")
public class PercentDiscount extends Discount {
    @Column(name = "min_condition")
    private BigDecimal minCondition;

    @Column(name = "discount_percent", precision = 3, scale = 2)
    private BigDecimal discountPercent;

    @Column(name = "max_discount")
    private BigDecimal maxDiscount;

}
