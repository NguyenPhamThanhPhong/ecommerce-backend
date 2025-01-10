package ecommerce.api.controller;

import ecommerce.api.dto.general.PaginationDTO;
import ecommerce.api.dto.general.SearchSpecification;
import ecommerce.api.dto.general.UserDetailDTO;
import ecommerce.api.dto.order.OrderCreateRequest;
import ecommerce.api.dto.order.OrderResponse;
import ecommerce.api.dto.order.OrderUpdateRequest;
import ecommerce.api.service.business.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/{code}")
    public ResponseEntity<?> getOrderById(@PathVariable Integer code) {
        return ResponseEntity.ok(orderService.findByCode(code));
    }
    @GetMapping("/me")
    public ResponseEntity<?> getMyOrders(@ParameterObject Pageable pageable, Authentication authentication) {
        UserDetailDTO detail = (UserDetailDTO) authentication.getPrincipal();
        return ResponseEntity.ok(orderService.getMyOrders(detail.getId(),pageable));
    }

    @PostMapping("/searches")
    public ResponseEntity<?> search(
            @ParameterObject Pageable pageable,
            @RequestBody Set<SearchSpecification> searchSpecs) {
        PaginationDTO<OrderResponse> categories = orderService.search(searchSpecs, pageable);
        return ResponseEntity.ok(categories);
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderCreateRequest request) {
        return ResponseEntity.ok(orderService.insert(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderById(@PathVariable UUID id, @RequestParam(required = false,defaultValue = "true") boolean isSoftDelete) {
        return ResponseEntity.ok(orderService.deleteOrderById(id, isSoftDelete));
    }

//    @PutMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> updateOrder(@RequestBody OrderUpdateRequest request) {
//        return ResponseEntity.ok(orderService.update(request));
//    }

}