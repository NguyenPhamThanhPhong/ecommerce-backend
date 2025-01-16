package ecommerce.api.mapper;

import ecommerce.api.dto.order.*;
import ecommerce.api.entity.product.Product;
import ecommerce.api.entity.transaction.Order;
import ecommerce.api.entity.transaction.OrderDetail;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
@MapperConfig(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface OrderMapper {

    OrderResponse fromEntityToResponse(Order order);

    OrderSelfResponse fromEntityToSelfResponse(Order order);

    Order fromUpdateRequestToEntity(OrderUpdateRequest request);

    OrderSingleResponse fromEntityToSingleResponse(Order order);

    @Mapping(target = "orderDetails",ignore = true)
    Order fromCreateRequestToEntity(OrderCreateRequest request);

    OrderDetailResponse.ProductSimpleResponse fromProductToProductSimpleResponse(Product product);


    default List<OrderDetail> fromListOrderDetailRequestToListOrderDetail(UUID orderId, List<OrderDetailRequest> requests) {
        return requests.stream()
                .map(request -> fromOrderDetailRequestToEntity(orderId, request))
                .toList();
    }

    default OrderDetail fromOrderDetailRequestToEntity(UUID orderId, OrderDetailRequest request) {
        return OrderDetail.builder()
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .orderId(orderId)
                .build();
    }

}
