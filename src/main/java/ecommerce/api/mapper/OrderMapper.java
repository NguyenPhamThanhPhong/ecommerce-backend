package ecommerce.api.mapper;

import ecommerce.api.dto.coupon.request.CouponCreateRequest;
import ecommerce.api.dto.coupon.request.CouponUpdateRequest;
import ecommerce.api.dto.coupon.response.CouponResponse;
import ecommerce.api.dto.order.OrderCreateRequest;
import ecommerce.api.dto.order.OrderResponse;
import ecommerce.api.dto.order.OrderUpdateRequest;
import ecommerce.api.entity.coupon.Coupon;
import ecommerce.api.entity.transaction.Order;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
@MapperConfig(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface OrderMapper {
    Order fromCreateRequestToEntity(OrderCreateRequest request);

    OrderResponse fromEntityToResponse(Order order);

    Order fromUpdateRequestToEntity(OrderUpdateRequest request);
}
