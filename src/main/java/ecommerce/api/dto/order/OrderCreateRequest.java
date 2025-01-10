package ecommerce.api.dto.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ecommerce.api.validation.annotation.IdentityValidation;
import ecommerce.api.validation.criteria.IdentityCriteria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@IdentityValidation(message = "creator of this order is not valid")
public class OrderCreateRequest implements IdentityCriteria {
    @JsonIgnore
    private UUID creatorId;

    private String address;

    private String status;

    private String notes;

    private String couponCode;

    private List<OrderDetailRequest> orderDetails;

    public void mergeOrderDetails() {
        this.orderDetails = orderDetails.stream()
                .collect(Collectors.groupingBy(OrderDetailRequest::getProductId,
                        Collectors.summingInt(OrderDetailRequest::getQuantity)))
                .entrySet()
                .stream()
                .map(entry -> new OrderDetailRequest(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public UUID getIdentity() {
        return this.creatorId;
    }

    @Override
    public void setIdentity(UUID identity) {
        this.setCreatorId(identity);
    }
}
