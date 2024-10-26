package ecommerce.api.dto.brand.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@Data
public class BrandUpdateRequest {

    private UUID id;

    private String description;

    private MultipartFile image;

    private String name;

}
