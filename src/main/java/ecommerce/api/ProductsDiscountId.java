package ecommerce.api;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class ProductsDiscountId implements java.io.Serializable {
    private static final long serialVersionUID = -7127350014875368675L;
    @NotNull
    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @NotNull
    @Column(name = "discount_id", nullable = false)
    private Integer discountId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductsDiscountId entity = (ProductsDiscountId) o;
        return Objects.equals(this.productId, entity.productId) &&
                Objects.equals(this.discountId, entity.discountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, discountId);
    }

}