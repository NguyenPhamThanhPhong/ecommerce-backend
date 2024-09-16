package ecommerce.api.entity.product;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

import java.util.List;

public class Category extends OutlineInfo {
    @ManyToMany
    @Transient
    private List<Brand> brands;

    @OneToMany
    @Transient
    private List<Product> products;
}
