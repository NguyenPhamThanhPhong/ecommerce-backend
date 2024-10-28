package ecommerce.api.dto.category.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@Data
public class CategoryUpdateRequest {

    private UUID id;

    private MultipartFile image;

    private String description;

    private String name;

}
