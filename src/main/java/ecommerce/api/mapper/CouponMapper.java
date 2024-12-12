package ecommerce.api.mapper;

import ecommerce.api.dto.coupon.request.CouponCreateRequest;
import ecommerce.api.dto.coupon.request.CouponUpdateRequest;
import ecommerce.api.dto.coupon.response.CouponResponse;
import ecommerce.api.entity.coupon.Coupon;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
@MapperConfig(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface CouponMapper {
    Coupon fromCreateRequestToEntity(CouponCreateRequest request);

    CouponResponse fromEntityToResponse(Coupon coupon);

    Coupon fromUpdateRequestToEntity(CouponUpdateRequest request);
}
