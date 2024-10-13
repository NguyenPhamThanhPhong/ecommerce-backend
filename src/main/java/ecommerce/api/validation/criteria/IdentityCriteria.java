package ecommerce.api.validation.criteria;

import java.util.UUID;

public interface IdentityCriteria {
    UUID getIdentity();
    void setIdentity(UUID identity);
}
