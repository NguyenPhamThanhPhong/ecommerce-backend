package ecommerce.api.repository;

import ecommerce.api.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface IProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findAllByDeletedAtIsNotNull(Pageable pageable);

    @Modifying
    @Query("DELETE FROM Product p WHERE p.id = :id")
    int deleteProductById(Integer id);



}
