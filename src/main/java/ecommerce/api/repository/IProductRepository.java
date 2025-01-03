package ecommerce.api.repository;

import ecommerce.api.entity.product.Product;
import ecommerce.api.entity.product.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public interface IProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    Page<Product> findAllByDeletedAtIsNotNull(Pageable pageable);

    @Modifying
    @Query("DELETE FROM Product p WHERE p.id = :id")
    int deleteProductById(UUID id);

    //    @Query("""
//            select p from Product p
//            left join fetch p.category
//            left join fetch p.brand
//            left join fetch p.productImages
//            """)
//    @Override
//    Page<Product> findAll(@Nullable Specification<Product> spec, Pageable pageable);
    @EntityGraph(attributePaths = {"brand", "category","productImages"})
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "insert into favorite_products (account_id, product_id) values (:accountId,:productId)", nativeQuery = true)
    int insertFavoriteProduct(UUID accountId, UUID productId);

    @Modifying
    @Transactional
    @Query(value = "delete from favorite_products where account_id = :accountId and product_id = :productId", nativeQuery = true)
    int deleteFavoriteProduct(UUID accountId, UUID productId);

    @Query(value = "select pi from ProductImage pi where pi.productId = :productId order by pi.seqNo asc")
    List<ProductImage> findProductImages(UUID productId);

    Optional<Product> findByCode(Integer code);

}
