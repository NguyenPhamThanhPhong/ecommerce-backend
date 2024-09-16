package ecommerce.api.entity.product;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Brand extends OutlineInfo {
    @ManyToMany
    @Transient
    private List<Category> category;
    @ManyToMany
    @Transient
    private List<Product> products;
}
