package ecommerce.api.mapper;


import ecommerce.api.dto.brand.request.BrandCreateRequest;
import ecommerce.api.dto.brand.request.BrandUpdateRequest;
import ecommerce.api.dto.brand.response.BrandResponse;
import ecommerce.api.entity.product.Brand;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring")
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BrandMapper {
    public Brand fromCreateRequestToEntity(BrandCreateRequest brandCreateRequest);
    public Brand fromUpdateRequestToEntity(BrandUpdateRequest brandUpdateRequest);

    public BrandResponse fromEntityToResponse(Brand brand);

}
