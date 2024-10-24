package ecommerce.api.controller;

import ecommerce.api.dto.product.request.ProductCreateRequest;
import ecommerce.api.dto.product.request.ProductUpdateRequest;
import ecommerce.api.entity.product.Product;
import ecommerce.api.mapper.ProductMapper;
import ecommerce.api.service.azure.CloudinaryService;
import ecommerce.api.service.business.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final CloudinaryService blobService;
    private final CloudinaryService cloudinaryService;

    @GetMapping("")
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.findById(id));
    }


    @PostMapping(value = "" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(@ModelAttribute ProductCreateRequest request) throws IOException {
        Product product = productMapper.fromCreateRequestToEntity(request);

        if(request.getImage() != null) {
            String imageUrl = storeFile(request.getImage() , product.getId());
            product.setImageUrl(imageUrl);
        }

        return ResponseEntity.ok(productService.upsertProduct(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.deleteProductById(id));
    }

    @PutMapping(value = "" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(@ModelAttribute ProductUpdateRequest request) throws IOException {
        Product product = productMapper.fromUpdateRequestToEntity(request);
        if(request.getImage() != null) {
            String imageUrl = storeFile(request.getImage() , product.getId());
            product.setImageUrl(imageUrl);
        }
        return ResponseEntity.ok(productService.upsertProduct(product));
    }


    private String storeFile(MultipartFile file , UUID id) throws IOException {
        return blobService.uploadFile(file,
                cloudinaryService.PRODUCT_DIR, id.toString());
    }

}
