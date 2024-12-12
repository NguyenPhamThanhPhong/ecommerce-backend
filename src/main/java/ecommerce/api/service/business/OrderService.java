package ecommerce.api.service.business;

import ecommerce.api.constants.OrderStatus;
import ecommerce.api.dto.order.OrderCreateRequest;
import ecommerce.api.dto.order.OrderResponse;
import ecommerce.api.dto.order.OrderUpdateRequest;
import ecommerce.api.entity.transaction.Order;
import ecommerce.api.exception.BadRequestException;
import ecommerce.api.exception.ResourceNotFoundException;
import ecommerce.api.mapper.OrderMapper;
import ecommerce.api.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final IOrderRepository orderRepository;
    private final OrderMapper orderMapper;


    public OrderResponse findById(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ORDER NOT FOUND"));
        return orderMapper.fromEntityToResponse(order);
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


    public int deleteOrderById(UUID id) {
        return orderRepository.deleteOrderById(id);
    }

}
