package ecommerce.api.repository;

import ecommerce.api.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface IProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {



    Page<Product> findAllByDeletedAtIsNotNull(Pageable pageable);

    @Modifying
    @Query("DELETE FROM Product p WHERE p.id = :id")
    int deleteProductById(UUID id);
}
