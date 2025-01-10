package ecommerce.api.service.business;

import ecommerce.api.dto.brand.response.BrandResponse;
import ecommerce.api.dto.general.PaginationDTO;
import ecommerce.api.dto.general.SearchSpecification;
import ecommerce.api.entity.product.Brand;
import ecommerce.api.mapper.BrandMapper;
import ecommerce.api.repository.IBrandRepository;
import ecommerce.api.utils.DynamicSpecificationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final IBrandRepository brandRepository;
    private final BrandMapper brandMapper;


    public PaginationDTO<BrandResponse> search(Set<SearchSpecification> searchSpecs, Pageable pageable) {
        Specification<Brand> finalSpecs = DynamicSpecificationUtils.buildSpecification(searchSpecs);
        Page<Brand> brands = brandRepository.findAll(finalSpecs, pageable);
        return PaginationDTO.fromPage(brands.map(brandMapper::fromEntityToResponse));
    }

    public BrandResponse getBrandByCode(long id) {
        return brandRepository.findBrandByCode(id).map(brandMapper::fromEntityToResponse).orElse(null);
    }

    public BrandResponse upsertBrand(Brand brand) {
        return brandMapper.fromEntityToResponse(brandRepository.save(brand));
    }

    public int deleteBrandById(UUID id) {
        return brandRepository.deleteBrandById(id);
    }
}
