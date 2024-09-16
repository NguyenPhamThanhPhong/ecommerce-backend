package ecommerce.api.entity.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ecommerce.api.entity.product.Product;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

public class OrderDetail {
    private String orderId;
    private int quantity;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @Transient
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @Transient
    private Product product;


}
