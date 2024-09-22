package ecommerce.api.entity.compositekey;


import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class OrderDetailId implements Serializable {
    private String orderId;
    private String productId;

    public OrderDetailId() {
    }

    public OrderDetailId(String orderId, String productId) {
        this.orderId = orderId;
        this.productId = productId;
    }
}
