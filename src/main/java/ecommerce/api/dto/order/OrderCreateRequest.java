package ecommerce.api.dto.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ecommerce.api.validation.annotation.IdentityValidation;
import ecommerce.api.validation.criteria.IdentityCriteria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

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

    private List<UUID> couponIds;

    private List<OrderDetailRequest> orderDetails;

    @Override
    public UUID getIdentity() {
        return null;
    }

    @Override
    public void setIdentity(UUID identity) {
        this.setCreatorId(identity);
    }
}
