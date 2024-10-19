package ecommerce.api.service.business;

import ecommerce.api.dto.general.ModificationResponse;
import ecommerce.api.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final IProductRepository productRepository;



}
