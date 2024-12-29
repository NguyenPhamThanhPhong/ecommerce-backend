package ecommerce.api.controller;
import ecommerce.api.config.property.CloudinaryProperties;
import ecommerce.api.dto.account.response.AccountResponse;
import ecommerce.api.dto.brand.request.BrandCreateRequest;
import ecommerce.api.dto.brand.request.BrandUpdateRequest;
import ecommerce.api.dto.brand.response.BrandResponse;
import ecommerce.api.dto.general.PaginationDTO;
import ecommerce.api.dto.general.SearchSpecification;
import ecommerce.api.entity.product.Brand;
import ecommerce.api.mapper.BrandMapper;
import ecommerce.api.service.azure.CloudinaryService;
import ecommerce.api.service.business.BrandService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;
    private final BrandMapper brandMapper;
    private final CloudinaryService cloudinaryService;
    private final CloudinaryProperties cloudinaryProperties;

    @PostMapping("searches")
    public ResponseEntity<?> getBrands(
            @ParameterObject Pageable pageable,
            @RequestBody Set<SearchSpecification> searchSpecs) {
        PaginationDTO<BrandResponse> brands = brandService.search(searchSpecs, pageable);
        return ResponseEntity.ok(brands);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getBrandById(@PathVariable UUID id) {
        return ResponseEntity.ok(brandService.getBrandById(id));
    }

    @PostMapping(value = "" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createBrand(@ModelAttribute BrandCreateRequest request) throws IOException {

        Brand brand = brandMapper.fromCreateRequestToEntity(request);

        if(request.getImage() != null) {
            String imageUrl = storeFile(request.getImage() , brand.getId());
            brand.setImageUrl(imageUrl);
        }

        return ResponseEntity.ok(brandService.upsertBrand(brand));
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
        return cloudinaryService.uploadFile(file,
                cloudinaryProperties.getBrandDir(), id.toString());
    }
}
