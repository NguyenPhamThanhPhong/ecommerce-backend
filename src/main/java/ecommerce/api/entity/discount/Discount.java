package ecommerce.api.entity.discount;

import ecommerce.api.entity.base.EntityBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "discounts")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "discount_type")
public class Discount extends EntityBase<Integer> {
    @Column(name = "title", length = Integer.MAX_VALUE)
    private String title;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "enable_date")
    private Date enableDate;

    @Column(name = "disable_date")
    private Date disableDate;

    @Column(name = "discount_type", insertable = false, updatable = false)
    private String discountType;  // Now this field is queryable
}
