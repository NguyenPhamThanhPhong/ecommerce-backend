package ecommerce.api.mapper;

import ecommerce.api.dto.product.request.ProductCreateRequest;
import ecommerce.api.dto.product.request.ProductImageRequest;
import ecommerce.api.dto.product.request.ProductUpdateRequest;
import ecommerce.api.dto.product.response.ProductResponse;
import ecommerce.api.entity.product.Product;
import ecommerce.api.entity.product.ProductImage;
import org.mapstruct.*;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
@MapperConfig(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ProductMapper {
    ProductResponse fromEntityToResponse(Product product);

    Product fromUpdateRequestToEntity(ProductUpdateRequest request);

    //    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())") // Generate UUID for Product
//    @Mapping(target = "productImages",
//            expression = "java(fromProductImageRequestListToEntityList(request.getProductImages(), product.getId()))")
    Product fromCreateRequestToEntity(ProductCreateRequest request);
    ProductImage fromImageRequestToImage(ProductImageRequest request);

//    default List<ProductImage> fromProductImageRequestListToEntityList(List<ProductImageRequest> request, UUID productId) {
//        return request.stream()
//                .map(r -> fromImageRequestToEntity(productId, r))
//                .toList();
//    }
//
//    default ProductImage fromImageRequestToEntity(UUID productId, ProductImageRequest request) {
//        ProductImage image = new ProductImage();
//        image.setProductId(productId);
//        image.setImageUrl(request.getImageUrl());
//        image.setName(request.getName());
//        image.setColour(request.getColour());
//        image.setSeqNo(request.getSeqNo());
//        return image;
//    }
}
