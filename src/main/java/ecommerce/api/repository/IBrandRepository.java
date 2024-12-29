package ecommerce.api.repository;

import ecommerce.api.entity.BlogPost;
import ecommerce.api.entity.product.Brand;
import ecommerce.api.entity.product.Product;
import ecommerce.api.entity.user.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface IBrandRepository  extends JpaRepository<Brand, UUID>, JpaSpecificationExecutor<Brand> {
    Page<Brand> findAllByDeletedAtIsNotNull(Pageable pageable);
//    @Override
//    @Query(value = "select a from Brand a",
//            countQuery = "select count(1) from Brand a")
//    Page<Brand> findAll(Specification specification, Pageable pageable);

    int deleteBrandById(UUID id);
}
