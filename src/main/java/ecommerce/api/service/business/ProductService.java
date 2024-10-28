package ecommerce.api.service.business;

import ecommerce.api.dto.category.response.CategoryResponse;
import ecommerce.api.dto.general.PaginationDTO;
import ecommerce.api.dto.general.SearchSpecification;
import ecommerce.api.dto.product.request.ProductCreateRequest;
import ecommerce.api.dto.product.request.ProductUpdateRequest;
import ecommerce.api.dto.product.response.ProductResponse;
import ecommerce.api.entity.product.Product;
import ecommerce.api.exception.ResourceNotFoundException;
import ecommerce.api.mapper.ProductMapper;
import ecommerce.api.repository.IProductRepository;
import ecommerce.api.service.azure.CloudinaryService;
import ecommerce.api.utils.DynamicSpecificationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final IProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CloudinaryService cloudinaryService;

    public PaginationDTO<ProductResponse> search(Set<SearchSpecification> searchSpec, Pageable pageable) {
        Specification<Product> spec = DynamicSpecificationUtils.buildSpecification(searchSpec);
        Page<Product> products = productRepository.findAll(spec, pageable);
        Page<ProductResponse> responses = products.map(productMapper::fromEntityToResponse);
        return PaginationDTO.fromPage(responses);
    }

    public ProductResponse findById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PRODUCT NOT FOUND"));
        return productMapper.fromEntityToResponse(product);
    }

    @Transactional
    public ProductResponse insert(ProductCreateRequest request) throws IOException {
        Product product = productMapper.fromCreateRequestToEntity(request);
        if(request.getImage()!=null)
        {
            String imageUrl = cloudinaryService.uploadFile(request.getImage(),cloudinaryService.PRODUCT_DIR,product.getId().toString());
            product.setImageUrl(imageUrl);
        }
        return productMapper.fromEntityToResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse update(ProductUpdateRequest request) throws IOException {
        Product product = productMapper.fromUpdateRequestToEntity(request);
        if(request.getImage()!=null)
        {
            cloudinaryService.deleteFile(product.getId().toString());
            String imageUrl = cloudinaryService.uploadFile(request.getImage(),cloudinaryService.PRODUCT_DIR,product.getId().toString());
            product.setImageUrl(imageUrl);
        }
        return productMapper.fromEntityToResponse(productRepository.save(product));
    }


    public int deleteProductById(UUID id) {
        return productRepository.deleteProductById(id);
    }
}
