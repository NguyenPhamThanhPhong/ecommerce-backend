package ecommerce.api.dto.projection;

import jakarta.persistence.Column;

import java.util.Map;

public interface ProductImageUrlsDTO {
    @Column(name = "image_urls")
    Map<String,String> getImageUrls();
}
