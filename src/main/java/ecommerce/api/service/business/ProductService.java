package ecommerce.api.service.business;

import ecommerce.api.dto.product.response.ProductResponse;
import ecommerce.api.entity.product.Product;
import ecommerce.api.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final IProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }


    public ProductResponse findById(UUID id) {
        return productRepository.findById(id).orElse(null);
    }
}
