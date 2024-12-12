package ecommerce.api.controller;

import ecommerce.api.dto.order.OrderCreateRequest;
import ecommerce.api.dto.order.OrderUpdateRequest;
import ecommerce.api.service.business.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;


    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @PostMapping(value = "" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createOrder(@RequestBody OrderCreateRequest request) {
        return ResponseEntity.ok(orderService.insert(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderById(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.deleteOrderById(id));
    }

    @PutMapping(value = "" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateOrder(@RequestBody OrderUpdateRequest request) {
        return ResponseEntity.ok(orderService.update(request));
    }

}