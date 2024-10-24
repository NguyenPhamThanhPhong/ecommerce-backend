package ecommerce.api.service.business;

import ecommerce.api.dto.product.response.ProductResponse;
import ecommerce.api.entity.product.Product;
import ecommerce.api.mapper.ProductMapper;
import ecommerce.api.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final IProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream().map(productMapper::fromEntityToProductResponse).toList();
    }


    public ProductResponse findById(UUID id) {
        return productMapper.fromEntityToProductResponse(productRepository.findById(id).orElse(null));
    }


    public ProductResponse upsertProduct(Product product) {
        return productMapper.fromEntityToProductResponse(productRepository.save(product));
    }

    public int deleteProductById(UUID id) {
        return productRepository.deleteProductById(id);
    }
}
