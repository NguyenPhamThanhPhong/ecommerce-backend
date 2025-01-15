package ecommerce.api.mapper;

import ecommerce.api.dto.product.request.ProductCreateRequest;
import ecommerce.api.dto.product.request.ProductUpdateRequest;
import ecommerce.api.dto.product.response.ProductChangesResponse;
import ecommerce.api.dto.product.response.ProductImageResponse;
import ecommerce.api.dto.product.response.ProductResponse;
import ecommerce.api.entity.product.Product;
import ecommerce.api.entity.product.ProductImage;
import ecommerce.api.utils.EntityUtils;
import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
@MapperConfig(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ProductMapper {
    ProductResponse fromEntityToResponse(Product product);

    Product fromUpdateRequestToEntity(ProductUpdateRequest request);

    //    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())") // Generate UUID for Product
    @Mapping(target = "productImages", ignore = true)
//            expression = "java(fromMultipartFilesToProductImages(product.getId(),request.getImages()))")
    Product fromCreateRequestToEntity(ProductCreateRequest request);

    ProductChangesResponse fromEntityToChangesResponse(Product product);

    ProductImageResponse fromImageToImageResponse(ProductImage image);

    default List<ProductImage> fromMultipartFilesToProductImages(UUID productId, List<MultipartFile> images) {
        List<ProductImage> productImages = new ArrayList<>(images.size());
        return productImages;
    }

    default ProductImage fromMultipartFileToProductImage(int i, MultipartFile image) {
        ProductImage productImage = new ProductImage();
        productImage.setName(image.getOriginalFilename());
        productImage.setColour(EntityUtils.getRandomColorHex());
        productImage.setSeqNo(i);
        return productImage;
    }
}
