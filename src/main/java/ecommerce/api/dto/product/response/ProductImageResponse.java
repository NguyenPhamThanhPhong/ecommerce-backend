package ecommerce.api.dto.product.response;

import ecommerce.api.entity.product.Product;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class ProductImageResponse {
    private int seqNo;

    private String name;

    private String colour;

    private Date createdAt;

    private Date deletedAt;

    private String imageUrl;
}
