package ecommerce.api.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {

    private String address;

    private String status;

    private String notes;

    private List<UUID> couponIds;

    private List<OrderDetailRequest> orderDetails;
}
