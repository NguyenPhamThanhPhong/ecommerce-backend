package ecommerce.api.controller;
import ecommerce.api.dto.brand.request.BrandCreateRequest;
import ecommerce.api.dto.brand.request.BrandUpdateRequest;
import ecommerce.api.entity.product.Brand;
import ecommerce.api.mapper.BrandMapper;
import ecommerce.api.service.azure.CloudinaryService;
import ecommerce.api.service.business.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;
    private final BrandMapper brandMapper;
    private final CloudinaryService blobService;
    private final CloudinaryService cloudinaryService;

    @GetMapping
    public ResponseEntity<?> getAllBrands() {
        return ResponseEntity.ok(brandService.getAllBrands());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getBrandById(@PathVariable UUID id) {
        return ResponseEntity.ok(brandService.getBrandById(id));
    }

    @PostMapping(value = "" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createBrand(@ModelAttribute BrandCreateRequest request) throws IOException {

        Brand brand = brandMapper.fromCreateRequestToEntity(request);

        if(request.getImage() != null) {
            String imageUrl = storeFile(request.getImage() , brand.getId());
            brand.setImageUrl(imageUrl);
        }

        return ResponseEntity.ok(brandService.upsertBrand(brand));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateBrand(@ModelAttribute BrandUpdateRequest request) throws IOException {
        Brand brand = brandMapper.fromUpdateRequestToEntity(request);

        if(request.getImage() != null) {
            String imageUrl = storeFile(request.getImage() , brand.getId());
            brand.setImageUrl(imageUrl);
        }

        return ResponseEntity.ok(brandService.upsertBrand(brand));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBrand(@PathVariable UUID id) {
        return ResponseEntity.ok(brandService.deleteBrandById(id));
    }


    private String storeFile(MultipartFile file , UUID id) throws IOException {
        return blobService.uploadFile(file,
                cloudinaryService.PRODUCT_DIR, id.toString());
    }
}
