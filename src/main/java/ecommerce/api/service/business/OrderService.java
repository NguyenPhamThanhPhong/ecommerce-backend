package ecommerce.api.service.business;

import ecommerce.api.constants.OrderStatus;
import ecommerce.api.constants.PaymentStatus;
import ecommerce.api.dto.general.PaginationDTO;
import ecommerce.api.dto.general.SearchSpecification;
import ecommerce.api.dto.order.*;
import ecommerce.api.entity.coupon.Coupon;
import ecommerce.api.entity.product.Product;
import ecommerce.api.entity.transaction.Order;
import ecommerce.api.entity.transaction.OrderDetail;
import ecommerce.api.entity.transaction.payment.Payment;
import ecommerce.api.exception.ResourceNotFoundException;
import ecommerce.api.mapper.OrderMapper;
import ecommerce.api.repository.IOrderDetailRepository;
import ecommerce.api.repository.IOrderRepository;
import ecommerce.api.repository.IPaymentRepository;
import ecommerce.api.repository.IProductRepository;
import ecommerce.api.utils.DynamicSpecificationUtils;
import ecommerce.api.utils.EntityUtils;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final IOrderRepository orderRepository;
    private final IOrderDetailRepository orderDetailRepository;
    private final IPaymentRepository paymentRepository;
    private final IProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final CouponService couponService;

    public OrderSingleResponse findByCode(Integer code) {
        Order order = orderRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("ORDER NOT FOUND"));
        List<UUID> productIds =order.getOrderDetails().stream()
                        .map(OrderDetail::getProductId)
                        .toList();
        Map<UUID, Product> productMap = productRepository.findAllByIdIn(productIds).stream().collect(Collectors.toMap(Product::getId, product -> product));
        order.getOrderDetails().forEach(detail -> detail.setProduct(
                productMap.getOrDefault(detail.getProductId(), null)
        ));
        return orderMapper.fromEntityToSingleResponse(order);
    }

    public PaginationDTO<OrderResponse> search(Set<SearchSpecification> searchSpecs, Pageable pageable) {
        Specification<Order> specs = DynamicSpecificationUtils.buildSpecification(searchSpecs);
        Page<Order> orders = orderRepository.findAll(specs, pageable);
        return PaginationDTO.fromPage(orders.map(orderMapper::fromEntityToResponse));
    }

    public PaginationDTO<OrderResponse> getMyOrders(UUID userId, Pageable pageable) {
        Page<Order> orders = orderRepository.findAllByCreatorId(userId, pageable);
        return PaginationDTO.fromPage(orders.map(orderMapper::fromEntityToResponse));
    }

    @Transactional
    public OrderResponse insert(OrderCreateRequest request) {
        request.mergeOrderDetails();
        Map<UUID, Integer> productQuantities = request.getOrderDetails().stream()
                .collect(Collectors.toMap(OrderDetailRequest::getProductId, OrderDetailRequest::getQuantity));
        List<UUID> productIds = new ArrayList<>(productQuantities.keySet());
        Collections.sort(productIds);
        List<Product> products = productRepository.findAllByIdIn(productIds);
        if (products.size() != productIds.size()) {
            throw new ResourceNotFoundException("PRODUCT NOT FOUND");
        }
        BigDecimal amount = products.stream()
                .map(product -> product.getPrice()
                        .multiply(BigDecimal.valueOf(productQuantities.get(product.getId())))
                        .multiply(BigDecimal.valueOf(1).subtract(product.getDiscountPercent().divide(BigDecimal.valueOf(100))))
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Order order = orderMapper.fromCreateRequestToEntity(request);
        if (request.getCouponCode() != null) {
            Pair<Coupon, BigDecimal> couponResult = couponService.evaluateDiscount(amount, request.getCouponCode());
            amount = couponResult.b;
            order.setCouponId(couponResult.a.getId());
        }
        order.setTotalValue(amount);
        Payment payment = EntityUtils.newPayment(request.getCreatorId(), order.getId(), amount);
        payment.setStatus(PaymentStatus.PENDING);
        orderRepository.insert(order);
        paymentRepository.save(payment);
        List<OrderDetail> orderDetails = orderMapper
                .fromListOrderDetailRequestToListOrderDetail(order.getId(), request.getOrderDetails());
        for (OrderDetail detail : orderDetails) {
            orderDetailRepository.insert(detail);
        }
        return orderMapper.fromEntityToResponse(order);
    }

    @Transactional
    public int deleteOrderById(UUID id, boolean isSoft) {
        if (isSoft) {
            return orderRepository.updateDeletedAtById(id);
        }
        return orderRepository.deleteOrderById(id);
    }

}
