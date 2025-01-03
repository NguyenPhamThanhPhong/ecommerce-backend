package ecommerce.api.service.business;

import ecommerce.api.constants.OrderStatus;
import ecommerce.api.dto.general.PaginationDTO;
import ecommerce.api.dto.general.SearchSpecification;
import ecommerce.api.dto.order.OrderCreateRequest;
import ecommerce.api.dto.order.OrderResponse;
import ecommerce.api.dto.order.OrderUpdateRequest;
import ecommerce.api.entity.transaction.Order;
import ecommerce.api.exception.BadRequestException;
import ecommerce.api.exception.ResourceNotFoundException;
import ecommerce.api.mapper.OrderMapper;
import ecommerce.api.repository.IOrderRepository;
import ecommerce.api.utils.DynamicSpecificationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final IOrderRepository orderRepository;
    private final OrderMapper orderMapper;


    public OrderResponse findByCode(Integer code) {
        Order order = orderRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("ORDER NOT FOUND"));
        return orderMapper.fromEntityToResponse(order);
    }

    public PaginationDTO<OrderResponse> search(Set<SearchSpecification> searchSpecs, Pageable pageable) {
        Specification<Order> specs = DynamicSpecificationUtils.buildSpecification(searchSpecs);
        Page<Order> orders = orderRepository.findAll(specs, pageable);
        return PaginationDTO.fromPage(orders.map(orderMapper::fromEntityToResponse));

    }

    @Transactional
    public OrderResponse insert(OrderCreateRequest request) {
        Order order = orderMapper.fromCreateRequestToEntity(request);

        return orderMapper.fromEntityToResponse(orderRepository.save(order));
    }

    @Transactional
    public OrderResponse update(OrderUpdateRequest request) {
        if(OrderStatus.valueOf(request.getStatus()) == OrderStatus.PENDING) {
            Order order = orderMapper.fromUpdateRequestToEntity(request);
            order.setId(request.getId());

            return orderMapper.fromEntityToResponse(orderRepository.save(order));
        } else {
            throw new BadRequestException("UPDATE IS NOT ALLOWED AS STATUS IS NOT PENDING");
        }
    }


    public int deleteOrderById(UUID id, boolean isSoft) {
        if(isSoft) {
            return orderRepository.updateDeletedAtById(id);
        }
        return orderRepository.deleteOrderById(id);
    }

}
