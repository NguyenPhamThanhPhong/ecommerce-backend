package ecommerce.api.validation.criteria;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

public interface IdentityCriteria {
    @JsonIgnore
    UUID getIdentity();
    @JsonIgnore
    void setIdentity(UUID identity);
}
