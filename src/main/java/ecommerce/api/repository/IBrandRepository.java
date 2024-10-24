package ecommerce.api.repository;

import ecommerce.api.entity.product.Brand;
import ecommerce.api.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IBrandRepository  extends JpaRepository<Brand, UUID> {
    Page<Brand> findAllByDeletedAtIsNotNull(Pageable pageable);

    int deleteBrandById(UUID id);
}
