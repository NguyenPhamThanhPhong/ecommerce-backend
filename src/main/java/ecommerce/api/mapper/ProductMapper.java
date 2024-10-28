package ecommerce.api.mapper;

import ecommerce.api.dto.account.request.AccountCreateRequest;
import ecommerce.api.dto.account.response.AccountResponse;
import ecommerce.api.dto.product.request.ProductCreateRequest;
import ecommerce.api.dto.product.request.ProductUpdateRequest;
import ecommerce.api.dto.product.response.ProductResponse;
import ecommerce.api.entity.product.Product;
import ecommerce.api.entity.user.Account;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;

@Mapper(componentModel = "spring")
@MapperConfig(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ProductMapper {
    Product fromCreateRequestToEntity(ProductCreateRequest request);

    ProductResponse fromEntityToProductResponse(Product product);

    Product fromUpdateRequestToEntity(ProductUpdateRequest request);
}
