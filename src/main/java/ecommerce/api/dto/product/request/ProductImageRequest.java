package ecommerce.api.dto.product.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductImageRequest {
    private int seqNo;
    private String name;
    private String colour;
    private MultipartFile image;

}
