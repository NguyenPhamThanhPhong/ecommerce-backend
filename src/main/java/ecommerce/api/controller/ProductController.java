package ecommerce.api.controller;

import ecommerce.api.service.business.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;


    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }


    public ResponseEntity<?> getProductById(UUID id) {
        return ResponseEntity.of(productService.findById(id));
    }

}
