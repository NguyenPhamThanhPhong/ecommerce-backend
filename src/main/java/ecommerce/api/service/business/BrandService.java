package ecommerce.api.service.business;

import ecommerce.api.dto.brand.response.BrandResponse;
import ecommerce.api.entity.product.Brand;
import ecommerce.api.mapper.BrandMapper;
import ecommerce.api.repository.IBrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final IBrandRepository brandRepository;
    private final BrandMapper brandMapper;

    public List<BrandResponse> getAllBrands() {
        return brandRepository.findAll().stream().map(brandMapper::fromEntityToResponse).toList();
    }

    public BrandResponse getBrandById(UUID id) {
        return brandRepository.findById(id).map(brandMapper::fromEntityToResponse).orElse(null);
    }

    public BrandResponse upsertBrand(Brand brand) {
        return brandMapper.fromEntityToResponse(brandRepository.save(brand));
    }

    public int deleteBrandById(UUID id) {
        return brandRepository.deleteBrandById(id);
    }
}
