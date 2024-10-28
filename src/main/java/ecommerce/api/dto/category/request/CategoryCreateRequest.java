package ecommerce.api.dto.category.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CategoryCreateRequest {

    private String description;

    private MultipartFile image;

    private String name;

}
