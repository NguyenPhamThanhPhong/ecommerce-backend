package ecommerce.api.entity.compositekey;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailId implements Serializable {
    private String orderId;
    private String productId;
}
