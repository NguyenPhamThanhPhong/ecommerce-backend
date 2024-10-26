package ecommerce.api.dto.brand.request;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BrandCreateRequest {

    private String description;

    private MultipartFile image;

    private String name;

}
