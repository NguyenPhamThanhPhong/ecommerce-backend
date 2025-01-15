package ecommerce.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class DataChangeResponse {
    private UUID id;
    private String imageUrl;
}
