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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
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

    @GetMapping("")
    public ResponseEntity<?> getProductsByCodesOrIds(
            @RequestParam(required = false) List<Integer> codes,
            @RequestParam(required = false) List<UUID> ids, @ParameterObject Pageable pageable) {
        PaginationDTO<ProductResponse> res = productService.findInCodesOrIds(codes, ids, pageable);
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
    @PreAuthorize("hasRole(T(ecommerce.api.constants.AuthRoleConstants).ROLE_DEFAULT)")
    public ResponseEntity<?> favoriteProduct(@RequestParam UUID productId, Authentication authentication) {
        UserDetailDTO auth = (UserDetailDTO) authentication.getPrincipal();
        productService.addFavorite(auth.getId(), productId);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/favorites")
    @PreAuthorize("hasRole(T(ecommerce.api.constants.AuthRoleConstants).ROLE_DEFAULT)")
    public ResponseEntity<?> unFavoriteProduct(@RequestParam UUID productId, Authentication authentication) {
        UserDetailDTO auth = (UserDetailDTO) authentication.getPrincipal();
        productService.removeFavorite(auth.getId(), productId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/favorites")
    @PreAuthorize("hasRole(T(ecommerce.api.constants.AuthRoleConstants).ROLE_DEFAULT)")
    public ResponseEntity<?> getFavoriteProducts(@ParameterObject Pageable pageable, Authentication authentication) {
        UserDetailDTO auth = (UserDetailDTO) authentication.getPrincipal();
        PaginationDTO<ProductResponse> res = productService.findFavorites(auth.getId(), pageable);
        return ResponseEntity.ok(res);
    }

}
