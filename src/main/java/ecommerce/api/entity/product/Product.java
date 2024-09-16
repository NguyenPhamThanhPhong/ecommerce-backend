package ecommerce.api.entity.product;

import ecommerce.api.entity.transaction.Order;
import ecommerce.api.entity.base.EntityBase;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;

import java.util.List;
import java.util.Map;

public class Product extends EntityBase<String> {
    private String name;
    private String description;
    private int stock;
    private float price;

    // key: PINK, value: image url of pink template
    // key: BIG-SIZE, value: image url of big-size template
    private Map<String,String> attributes;


    private Category category;
    private Brand brand;
    private List<Tag> tags;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Order> orders;
}
