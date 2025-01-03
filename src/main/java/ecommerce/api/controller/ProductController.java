package ecommerce.api.controller;

import ecommerce.api.dto.general.PaginationDTO;
import ecommerce.api.dto.general.SearchSpecification;
import ecommerce.api.dto.general.UserDetailDTO;
import ecommerce.api.dto.product.request.ProductCreateRequest;
import ecommerce.api.dto.product.request.ProductUpdateRequest;
import ecommerce.api.dto.product.response.ProductResponse;
import ecommerce.api.service.business.ProductService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/searches")
    public ResponseEntity<?> getAllProducts(
            @ParameterObject Pageable pageable,
            @RequestBody(required = false) Set<SearchSpecification> specs) {
        PaginationDTO<ProductResponse> res = productService.search(specs, pageable);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getProductById(@PathVariable int code) {
        return ResponseEntity.ok(productService.findByCode(code));
    }

    @PostMapping(value = "" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(@ModelAttribute ProductCreateRequest request) throws IOException {
        return ResponseEntity.ok(productService.insert(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.deleteProductById(id));
    }

    @PutMapping(value = "" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(@ModelAttribute ProductUpdateRequest request) throws IOException {
        return ResponseEntity.ok(productService.update(request));
    }

    @PutMapping("/favorites")
    public ResponseEntity<?> favoriteProduct(@RequestParam UUID productId, Authentication authentication) {
        UserDetailDTO auth = (UserDetailDTO) authentication.getPrincipal();
        productService.addFavoriteProduct(auth.getId(), productId);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/favorites")
    public ResponseEntity<?> unFavoriteProduct(@RequestParam UUID productId, Authentication authentication) {
        UserDetailDTO auth = (UserDetailDTO) authentication.getPrincipal();
        productService.removeFavoriteProduct(auth.getId(), productId);
        return ResponseEntity.ok().build();
    }

}
