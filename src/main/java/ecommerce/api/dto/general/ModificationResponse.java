package ecommerce.api.dto.general;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@AllArgsConstructor
@Data
public class ModificationResponse<T> {
    private T id;
    private Date createdAt;
    @JsonUnwrapped
    private Map<String, Object> changes;

    public ModificationResponse(T id, Date createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }
}
