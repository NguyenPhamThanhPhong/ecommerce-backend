package ecommerce.api.dto.brand.response;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class BrandResponse {

    private UUID id;

    private long code;

    private Date createdAt;
    private Date deletedAt;

    private String description;

    private String imageUrl;

    private String name;

    private int stock;

    private int sold;

}
